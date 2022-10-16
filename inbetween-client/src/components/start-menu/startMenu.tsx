import React, { useEffect, useState, useMemo } from "react";
import axios from "axios";
import StompJS from "stompjs";
import SockJS from "sockjs-client";
import JoinAbleGames from "../../models/joinableGames";

export interface StartMenuProps {}

export const StartMenu: React.FC<StartMenuProps> = (props: StartMenuProps) => {
  const [joinAbleGameResponse, setJoinAbleGameResponse] = useState<
    JoinAbleGames[]
  >();

  useEffect(() => {
    const socket: any = new SockJS("/gs-guide-websocket"),
      stomp = StompJS.over(socket);

    stomp.connect({}, () => {
      stomp.subscribe("/topic/new-lobby", (message: any) => {
        message.body;
        setJoinAbleGameResponse(prevState => {
          let arr = [...prevState];
          let newLobby: JoinAbleGames = JSON.parse(message.body);
          arr.unshift(newLobby);
          return arr;
        });
      });
    });

    return () => socket.close();
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
      </tr>
    ));
  }, [joinAbleGameResponse]);

  return (
    <>
      <h2>Lobbies to join</h2>
      <table>
        <thead></thead>
        <tbody>{gameSelections}</tbody>
      </table>
    </>
  );
};
