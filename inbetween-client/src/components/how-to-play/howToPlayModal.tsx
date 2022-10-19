import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";

export interface HowToPlayModalProps {
  show: boolean;
  handleCloseModal: () => void;
}

export const HowToPlayModal: React.FC<HowToPlayModalProps> = (
  props: HowToPlayModalProps
) => {
  return (
    <>
      <Modal
        id="how-to-play-modal"
        show={props.show}
        onHide={props.handleCloseModal}
        size={"lg"}
      >
        <Modal.Header closeButton>
          <Modal.Title>How to play: "In-Between Poker"</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="section-wrapper">
            <h6 className="section-heading">Getting Started</h6>
            <p>
              Use this current page to either <i>"Start a new game"</i> or{" "}
              <i>"Join"</i> a game that is accepting players.
              <ul>
                <li>
                  When you create a new lobby, you need to specify a display
                  name for yourself, as well as a name for the game.
                </li>
                <li>
                  If you wish to join a game, there is an input above the listed
                  games. Please enter a display name and join the game of your
                  choosing.
                </li>
              </ul>
            </p>
            <p>
              Once a lobby is created, it will be in "OPEN" status.
              <br />
              Any player within the lobby can click "Start Game", and from that
              point on, no one else is allowed to join.
            </p>
          </div>
          <div className="section-wrapper">
            <h6 className="section-heading">What are we betting on?</h6>
            <p>
              When you place a bet, you are betting that the next card shown,
              will be <b>IN-BETWEEN</b> the two currently shown cards. If the
              card that is revealed meets that criteria, you get your bet amount
              back doubled. (IE Bet 25 to win 50 as an example). If the card
              that is reveal is outside of the two showing cards, you lose the
              amount you bid. But with no penalty. If the card revealed is the
              same value as one of the outer cards, a 2x penalty is applied.
              This is also why you cannot bet more than half of your points.
            </p>
          </div>
          <div className="section-wrapper">
            <h6 className="section-heading">Rules to the Game</h6>
            <p>
              Each player is gifted is 100 points at the start of the game. For
              every player that is in the lobby, there will be 300 points added
              to the pot that are up for grabs. The goal of the game is to have
              the most points once the pot runs out. At the start of each turn,
              two side cards will appear on the screen and the user is faced
              with two options. [BET or PASS] Depending on which option is
              chosen, the game with either process the bet, or reroll cards and
              pass to the new person. There is an arrow icon next to the players
              list that annotates whos current turn it is. The number in '(x)'
              is the current points for that user. At the bottom there is a
              gauge and number representing 'pot total', this is how many point
              are up for grab at the current time of play. <br />
              <br />
              Player Options:
              <ul id="betting-explanation">
                <li>BET - </li>
                <li>ADVANCE - </li>
                <li>PASS - </li>
              </ul>
            </p>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={props.handleCloseModal}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};
