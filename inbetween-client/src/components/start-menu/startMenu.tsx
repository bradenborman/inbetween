import React, { useEffect, useState, useMemo, useRef } from "react";
import axios from "axios";
import StompJS from "stompjs";
import SockJS from "sockjs-client";
import { useHistory } from "react-router-dom";
import JoinableGames from "../../models/joinableGames";
import {
  Container,
  Col,
  Row,
  Table,
  Button,
  Form,
  FormControl
} from "react-bootstrap";

export interface StartMenuProps {}

export const StartMenu: React.FC<StartMenuProps> = (props: StartMenuProps) => {
  let history = useHistory();

  const playerNameRef = useRef(null);
  const lobbyNameRef = useRef(null);

  const [joinGameDisplayName, setJoinGameDisplayName] = useState<string>(""); //Cant use ref for ths one

  const [joinAbleGameResponse, setJoinAbleGameResponse] = useState<
    JoinableGames[]
  >();
  const [submittingNewGame, setSubmittingNewGame] = useState<boolean>(false);

  useEffect(() => {
    const webSocket: WebSocket = new SockJS("/gs-guide-websocket");
    const stomp: StompJS.Client = StompJS.over(webSocket);
    stomp.connect({}, () => {
      stomp.subscribe("/topic/new-lobby", (message: any) => {
        if (!submittingNewGame) {
          setJoinAbleGameResponse(prevState => {
            let arr = [...prevState];
            let newLobby: JoinableGames = JSON.parse(message.body);
            arr.unshift(newLobby);
            return arr;
          });
        }
      });
      stomp.subscribe("/topic/joinable-game-update", (message: any) => {
        setJoinAbleGameResponse(prevState => JSON.parse(message.body));
      });
    });

    return () => webSocket.close();
  }, []);

  useEffect(() => {
    axios.get("/api/joinable-games").then(response => {
      setJoinAbleGameResponse(response.data);
    });
  }, []);

  const gameSelections: JSX.Element[] | null = useMemo(() => {
    return joinAbleGameResponse?.map((x, index) => (
      <tr key={index}>
        <td>{x.lobbyName}</td>
        <td className="center">{x.gameStatus}</td>
        <td className="center">
          <Button
            className="btn-link"
            onClick={e => joinLobby(x.gameId, joinGameDisplayName)}
            disabled={
              joinGameDisplayName == "" || joinGameDisplayName.length < 3
            }
          >
            Join
          </Button>
        </td>
      </tr>
    ));
  }, [joinAbleGameResponse, joinGameDisplayName]);

  const joinLobby = (gameIdToJoin: Number, displayName: string) => {
    if (displayName != "" && displayName.length > 2) {
      axios
        .post("/perform:JOIN_LOBBY", {
          displayName,
          gameId: gameIdToJoin,
          playerRole: "PLAYER",
          joinGame: true
        })
        .then(response => {
          if (response.status == 200) {
            history.push({
              pathname: "/game",
              search: "?lobby=" + response.data.uuid,
              state: {
                validRedirect: true,
                userIdJoined: response.data.playerIdCreated,
                gameUUID: response.data.uuid
              }
            });
          }
        });
    }
  };

  const handleJoinGameDisplayNameChange = (e: any) => {
    setJoinGameDisplayName(prev => e.target.value);
  };

  const handleNewLobbySubmit = (e: any) => {
    e.preventDefault();
    const displayNameFromRef: string = playerNameRef.current.value;
    const lobbyName: string = lobbyNameRef.current.value;
    setSubmittingNewGame(true);
    axios
      .post("/perform:CREATE_LOBBY", {
        displayName: displayNameFromRef,
        lobbyName,
        userRole: "PLAYER"
      })
      .then(response => {
        if (response.status == 201) {
          history.push({
            pathname: "/game",
            search: "?lobby=" + response.data.uuid,
            state: {
              validRedirect: true,
              userIdJoined: response.data.userPlayingOnScreenId,
              gameUUID: response.data.uuid
            }
          });
        }
        setSubmittingNewGame(false);
      });
  };

  const joinGameSection: JSX.Element = useMemo(() => {
    if (joinAbleGameResponse == undefined || joinAbleGameResponse.length == 0) {
      return (
        <>
          There are 0 joinable lobbies to join at this time. Please create one
          one or wait for one to be created.
        </>
      );
    }

    return (
      <>
        <Form.Label htmlFor="joinGameDisplayName">
          Join game display name
        </Form.Label>
        <FormControl
          id="joinGameDisplayName"
          onChange={handleJoinGameDisplayNameChange}
          value={joinGameDisplayName}
        />
        <Table bordered className="join-lobby-table">
          <thead>
            <tr>
              <th>Lobby Name</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>{gameSelections}</tbody>
        </Table>
      </>
    );
  }, [joinAbleGameResponse, joinGameDisplayName]);

  return (
    <main id="start-menu">
      <Container>
        <Row>
          <Col md={6}>{joinGameSection}</Col>
          <Col md={6} id="create-new-game-form-col">
            <Form onSubmit={handleNewLobbySubmit}>
              <fieldset>
                <Form.Group className="mb-3">
                  <Form.Label htmlFor="playerName">Player's Name</Form.Label>
                  <Form.Control
                    required
                    ref={playerNameRef}
                    id="playerName"
                    placeholder="How name will appear in game"
                  />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label htmlFor="lobbyName">Lobby Name</Form.Label>
                  <Form.Control
                    required
                    ref={lobbyNameRef}
                    id="lobbyName"
                    placeholder="Text Friends will search for"
                  />
                </Form.Group>
                <Button type="submit">Create Lobby</Button>
              </fieldset>
            </Form>
          </Col>
        </Row>
      </Container>
    </main>
  );
};
