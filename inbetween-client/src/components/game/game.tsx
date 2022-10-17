import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import StompJS from "stompjs";
import SockJS from "sockjs-client";

export interface GameProps {}

export const Game: React.FC<GameProps> = (props: GameProps) => {
  const location = useLocation();

  const [joinedGameFromHomePage, setJoinedGameFromHomePage] = useState<
    boolean
  >();

  useEffect(() => {
    const state: any = location.state;
    setJoinedGameFromHomePage(state?.validRedirect);
  }, [location]);

  useEffect(() => {
    const state: any = location.state;
    const userIDJoined = state?.userIdJoined;

    const webSocket: WebSocket = new SockJS("/gs-guide-websocket");
    const stomp: StompJS.Client = StompJS.over(webSocket);
    stomp.connect({}, () => {
      stomp.subscribe("/topic/user-joined-game", (message: any) => {
        const player: any = JSON.parse(message.body);
        console.log(player);
      });
      stomp.send("/app/joined", {}, userIDJoined);
    });

    return () => webSocket.close();
  }, []);

  return (
    <main>
      <h2>test</h2>
    </main>
  );
};
