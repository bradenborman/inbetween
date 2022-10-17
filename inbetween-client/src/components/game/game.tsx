import React, { useEffect, useState, useMemo } from "react";
import { useLocation, useHistory } from "react-router-dom";
import classNames from "classnames";
import StompJS from "stompjs";
import SockJS from "sockjs-client";
import JoinedGameResponse from "../../models/joinedGameResponse";
import Player from "../../models/player";
import axios from "axios";
import { Container, Row, Col, Card } from "react-bootstrap";
import GameStatusResponse from "../../models/gameStatusResponse";

export interface GameProps {}

export const Game: React.FC<GameProps> = (props: GameProps) => {
  const location = useLocation();
  const history = useHistory();

  const [userId, setUserId] = useState<string>();
  const [gameUUID, setGameUUID] = useState<string>();
  const [gameStatusResponse, setGameStatusResponse] = useState<
    GameStatusResponse
  >();

  const [playerList, setPlayerList] = useState<Player[]>();

  useEffect(() => {
    const state: any = location.state;
    //Check to see if load came from home page with state passed
    if (!state?.validRedirect) {
      history.push({
        pathname: "/",
        search: "?message=cannot-join-game-at-this-point"
      });
    } else {
      axios
        .get(`/api/player-list-by-uuid?uuid=${state.gameUUID}`)
        .then(response => {
          setPlayerList(prev => response.data);
        });

      setUserId(state.userIdJoined);
      setGameUUID(state.gameUUID);
    }
  }, [location]);

  useEffect(() => {
    const state: any = location.state;
    if (!state?.validRedirect) {
      history.push({
        pathname: "/",
        search: "?message=cannot-join-game-at-this-point"
      });
    } else {
      axios.get(`/api/game-status?uuid=${state.gameUUID}`).then(response => {
        const status: GameStatusResponse = response.data;
        setGameStatusResponse(status);
      });
    }
  }, []);

  useEffect(() => {
    const webSocket: WebSocket = new SockJS("/gs-guide-websocket");

    if (gameUUID != undefined && userId != undefined) {
      const stomp: StompJS.Client = StompJS.over(webSocket);
      stomp.connect({}, () => {
        stomp.subscribe("/topic/update-player-list", (message: any) => {
          let playerJoined: JoinedGameResponse = JSON.parse(message.body);
          if (playerJoined.uuid == gameUUID) {
            setPlayerList(prev => playerJoined.playersJoined);
          }
        });
        stomp.subscribe("/topic/update-game-status", (message: any) => {
          let gameStatusResponse: GameStatusResponse = JSON.parse(message.body);
          if (gameStatusResponse.uuid == gameUUID) {
            setGameStatusResponse(prev => gameStatusResponse);
          }
        });
      });
    }

    return () => webSocket.close();
  }, [gameUUID, userId]);

  const playersWaitingRows: JSX.Element[] | null = useMemo(() => {
    console.log(playerList);
    return playerList?.map((x, index) => (
      <tr key={index}>
        <td className={classNames({ "my-user": x.userId == userId })}>
          ({x.score}) - {x.displayName}
        </td>
        <td className={"turn-status"}>{x.playersTurn ? "Users Turn" : ""}</td>
      </tr>
    ));
  }, [playerList]);

  return (
    <main id="gamelobby">
      <Container>
        <Row>
          <Col lg={9}>
            <Card>
              <div id="game-table-wrapper">
                <table id="game-board">
                  <tbody>
                    <tr id="cards">
                      <td>
                        <img src="/img/cards/backs/red2.png" />
                      </td>
                      <td></td>
                      <td>
                        <img src="/img/cards/backs/red2.png" />
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </Card>
          </Col>
          <Col lg={3}>
            <Card id="game-status-card">
              Game Status: {gameStatusResponse?.gameStatus}
            </Card>
            <Card>
              <table id="player-turn-order">
                <tbody>{playersWaitingRows}</tbody>
              </table>
            </Card>
          </Col>
        </Row>
      </Container>
    </main>
  );
};
