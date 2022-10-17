import React from "react";
import {
  BrowserRouter as ReactRouter,
  Route as ReactRoute
} from "react-router-dom";
import { StartMenu } from "../start-menu/startMenu";
import { Game } from "../game/game";

require("./app.scss");

export interface AppProps {}

export const App: React.FC<AppProps> = (props: AppProps) => {
  return (
    <ReactRouter>
      <ReactRoute path="/" exact component={StartMenu} />
      <ReactRoute path="/game" exact component={Game} />
    </ReactRouter>
  );
};
