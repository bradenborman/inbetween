import React from "react";
import { Navbar } from "../navbar/navbar";
import { BrowserRouter as Router, Route } from "react-router-dom";

require("./inbwtweenApp.scss");
export interface InbwtweenAppProps {}

export const InbwtweenApp: React.FC<InbwtweenAppProps> = (
  props: InbwtweenAppProps
) => {
  
  const getHomePage = (): JSX.Element => {
    return (
    <div>
      <p>Site Under Construction
        <br />
        <i className="fas fa-tools" />
      </p>
    </div>
    )
  };

  const getRulesPage = (): JSX.Element => {
    return <p>Rules</p>;
  };

  return (

      <div id="inbetween-app">
        <Navbar />
        {getHomePage()}
      </div>
  );

};