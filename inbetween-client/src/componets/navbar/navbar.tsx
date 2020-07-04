import React from "react";

export interface NavbarProps {}

export const Navbar: React.FC<NavbarProps> = (props: NavbarProps) => {
  return (
    <div id="navbar">
      <div id="navbar-brand">
        <div id="navbar-title">
          <h1>
            Borman Presents: <span className="highlight">In-between Poker</span>
          </h1>
        </div>
      </div>
    </div>
  );
};
