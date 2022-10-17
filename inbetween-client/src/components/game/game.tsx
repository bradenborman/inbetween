import React, { useEffect, useState } from "react";
import { useLocation, useHistory } from "react-router-dom";
import StompJS from "stompjs";
import SockJS from "sockjs-client";

export interface GameProps {}

export const Game: React.FC<GameProps> = (props: GameProps) => {
  const location = useLocation();
  const history = useHistory();

  const [userId, setUserId] = useState<string>();

  useEffect(() => {
    const state: any = location.state;

    //Check to see if load came from home page with state passed
    if (!state?.validRedirect) {
      history.push({
        pathname: "/",
        search: "?message=cannot-join-game-at-this-point"
      });
    }

    setUserId(state.userIdJoined);
  }, [location]);

  return (
    <main>
      <h2>UserId: {userId}</h2>
    </main>
  );
};
