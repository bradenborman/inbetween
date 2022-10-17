import React, { useEffect, useState } from "react";
import { useLocation, useHistory } from "react-router-dom";
import StompJS from "stompjs";
import SockJS from "sockjs-client";
import JoinedGameResponse from "../../models/joinedGameResponse";

export interface GameProps {}

export const Game: React.FC<GameProps> = (props: GameProps) => {
  const location = useLocation();
  const history = useHistory();

  const [userId, setUserId] = useState<string>();
  const [userDisplayName, setUserDisplayName] = useState<string>();
  const [gameUUID, setGameUUID] = useState<string>();

  //NEED TO GET THE LIST OF PLAYERS IN THE LOBBY AND THEN ADD TO IT AS THEY COME IN

  useEffect(() => {
    const state: any = location.state;
    //Check to see if load came from home page with state passed
    if (!state?.validRedirect) {
      history.push({
        pathname: "/",
        search: "?message=cannot-join-game-at-this-point"
      });
    }

    console.log(state);

    setUserId(state.userIdJoined);
    setUserDisplayName(state.displayName);
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
            alert("New player joined: " + playerJoined.displayName);
          }
        });
      });
    }

    return () => webSocket.close();
  }, [gameUUID, undefined]);

  return (
    <main>
      <h2>UserId: {userDisplayName}</h2>
    </main>
  );
};
