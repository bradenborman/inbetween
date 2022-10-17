import React, { useEffect, useState, useMemo } from "react";
import { useLocation, useHistory } from "react-router-dom";
import StompJS from "stompjs";
import SockJS from "sockjs-client";
import JoinedGameResponse from "../../models/joinedGameResponse";
import Player from "../../models/player";
import axios from "axios";

export interface GameProps {}

export const Game: React.FC<GameProps> = (props: GameProps) => {
  const location = useLocation();
  const history = useHistory();

  const [userId, setUserId] = useState<string>();
  const [gameUUID, setGameUUID] = useState<string>();

  const [playerList, setPlayerList] = useState<Player[]>();

  const playersWaitingRows: JSX.Element[] | null = useMemo(() => {
    return playerList?.map((x, index) => (
      <tr key={index}>
        <td>{x.displayName}</td>
      </tr>
    ));
  }, [playerList]);

  useEffect(() => {
    const state: any = location.state;
    //Check to see if load came from home page with state passed
    if (!state?.validRedirect) {
      history.push({
        pathname: "/",
        search: "?message=cannot-join-game-at-this-point"
      });
    }

    axios
      .get(`/api/player-list-by-uuid?uuid=${state.gameUUID}`)
      .then(response => {
        setPlayerList(prev => response.data);
      });

    setUserId(state.userIdJoined);
    setGameUUID(state.gameUUID);
  }, [location]);

  useEffect(() => {
    const webSocket: WebSocket = new SockJS("/gs-guide-websocket");

    if (gameUUID != undefined && userId != undefined) {
      const stomp: StompJS.Client = StompJS.over(webSocket);
      stomp.connect({}, () => {
        stomp.subscribe("/topic/user-joined-game", (message: any) => {
          let playerJoined: JoinedGameResponse = JSON.parse(message.body);
          if (playerJoined.uuid == gameUUID) {
            setPlayerList(prev => playerJoined.playersJoined);
          }
        });
      });
    }

    return () => webSocket.close();
  }, [gameUUID, undefined]);

  return (
    <main>
      <table>
        <tbody>
          <tr>
            <th>Name</th>
          </tr>
          {playersWaitingRows}
        </tbody>
      </table>
    </main>
  );
};
