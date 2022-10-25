import React from "react";
import { Navbar, Container } from "react-bootstrap";
import { Link } from "react-router-dom";

export interface NavbarProps {}

export const NavigationBar: React.FC<NavbarProps> = (props: NavbarProps) => {
  return (
    <Navbar id="navbar" bg="dark">
      <Container>
        <Link to={"/"}>In-Between Poker</Link>
      </Container>
    </Navbar>
  );
};
