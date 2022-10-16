import React from "react";
import {
  BrowserRouter as ReactRouter,
  Route as ReactRoute
} from "react-router-dom";
import { StartMenu } from "../start-menu/startMenu";

require("./app.scss");

export interface AppProps {}

export const App: React.FC<AppProps> = (props: AppProps) => {
  return (
    <ReactRouter>
      <ReactRoute path="/">
        <StartMenu />
      </ReactRoute>
    </ReactRouter>
  );
};
