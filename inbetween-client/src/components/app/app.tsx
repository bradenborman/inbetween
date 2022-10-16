import React from "react";
import {
  BrowserRouter as ReactRouter,
  Route as ReactRoute
} from "react-router-dom";

require("./app.scss");

export interface AppProps {}

export const App: React.FC<AppProps> = (props: AppProps) => {
  return (
    <ReactRouter>
      <ReactRoute path="/">
        <h1>TEST</h1>
      </ReactRoute>
    </ReactRouter>
  );
};
