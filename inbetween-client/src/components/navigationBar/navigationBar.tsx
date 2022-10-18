import React from "react";
import { Navbar, Container } from "react-bootstrap";

export interface NavbarProps {}

export const NavigationBar: React.FC<NavbarProps> = (props: NavbarProps) => {
  return (
    <Navbar id="navbar" bg="dark">
      <Container>
        <span>In-Between Poker</span>
      </Container>
    </Navbar>
  );
};
