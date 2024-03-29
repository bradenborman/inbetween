import React, { useEffect, useState, useMemo, useRef } from "react";
import { useLocation, useHistory } from "react-router-dom";
import classNames from "classnames";
import StompJS from "stompjs";
import SockJS from "sockjs-client";
import JoinedGameResponse from "../../models/joinedGameResponse";
import Player from "../../models/player";
import axios from "axios";
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  ProgressBar,
  InputGroup,
  Form
} from "react-bootstrap";
import GameStatusResponse from "../../models/gameStatusResponse";
import GameUpdateStartTurn from "../../models/gameUpdateStartTurn";
import PlayingCard from "../../models/playingCard";
import BetResult from "../../models/betResult";
import SplitResponse from "../../models/splitResponse";

export interface GameProps {}

export const Game: React.FC<GameProps> = (props: GameProps) => {
  const location = useLocation();
  const history = useHistory();

  // const dummyCard: PlayingCard = {
  //   cardId: "",
  //   cardValue: "5",
  //   suit: "CLUBS"
  // }

  const bidAmountRef = useRef(null);

  const [userId, setUserId] = useState<string>();
  const [gameUUID, setGameUUID] = useState<string>();
  const [gameStatusResponse, setGameStatusResponse] = useState<
    GameStatusResponse
  >();

  const [playerList, setPlayerList] = useState<Player[]>();
  const [potTotal, setPotTotal] = useState<number>();
  const [leftPlayingCard, setLeftPlayingCard] = useState<PlayingCard>();
  const [rightPlayingCard, setRightPlayingCard] = useState<PlayingCard>();
  const [middlePlayingCard, setMiddlePlayingCard] = useState<PlayingCard>();
  const [maxBidAllowed, setMaxBidAllowed] = useState<number>(0);
  const [cardsUntilReshuffle, setCardsUntilReshuffle] = useState<number>(52);

  //Split turn vars
  const [splitTurn, setSplitTurn] = useState<boolean>(false);
  const [leftSplitPlayingCard, setLeftSplitPlayingCard] = useState<
    PlayingCard
  >();
  const [rightSplitPlayingCard, setRightSplitPlayingCard] = useState<
    PlayingCard
  >();

  const [waitingAcknowledgeResults, setWaitingAcknowledgeResults] = useState<
    boolean
  >(false);
  const [betResult, setBetResult] = useState<BetResult>();

  useEffect(() => {
    const state: any = location.state;
    //Check to see if load came from home page with state passed
    if (!state?.validRedirect) {
      history.push({
        pathname: "/",
        search: "?message=cannot-join-game-at-this-point"
      });
    } else {
      axios
        .get(`/api/player-list-by-uuid?uuid=${state.gameUUID}`)
        .then(response => {
          setPlayerList(prev => response.data);
        });

      setUserId(state.userIdJoined);
      setGameUUID(state.gameUUID);
    }
  }, [location]);

  useEffect(() => {
    const state: any = location.state;
    if (!state?.validRedirect) {
      history.push({
        pathname: "/",
        search: "?message=cannot-join-game-at-this-point"
      });
    } else {
      axios.get(`/api/game-status?uuid=${state.gameUUID}`).then(response => {
        const status: GameStatusResponse = response.data;
        setGameStatusResponse(status);
      });

      axios
        .get(`/api/latest-turn-update?uuid=${state.gameUUID}`)
        .then(response => {
          const turnUpdate: GameUpdateStartTurn = response.data;
          setPotTotal(turnUpdate.potTotal);
          setLeftPlayingCard(turnUpdate.leftPlayingCard);
          setRightPlayingCard(turnUpdate.rightPlayingCard);
          setMaxBidAllowed(turnUpdate.maxBidAllowed);
          setPlayerList(turnUpdate.playerList);
          setCardsUntilReshuffle(turnUpdate.cardsLeftUntilReshuffle);
        });
    }
  }, []);

  useEffect(() => {
    const webSocket: WebSocket = new SockJS("/gs-guide-websocket");

    if (gameUUID != undefined && userId != undefined) {
      const stomp: StompJS.Client = StompJS.over(webSocket);
      stomp.connect({}, () => {
        stomp.subscribe(
          "/topic/user-connected/" + userId,
          (message: any) => {}
        );
        stomp.subscribe("/topic/update-player-list", (message: any) => {
          let playerJoined: JoinedGameResponse = JSON.parse(message.body);
          if (playerJoined.uuid == gameUUID) {
            setPlayerList(prev => playerJoined.playersJoined);
          }
        });
        stomp.subscribe("/topic/update-game-status", (message: any) => {
          let gameStatusResponse: GameStatusResponse = JSON.parse(message.body);
          if (gameStatusResponse.uuid == gameUUID) {
            setGameStatusResponse(prev => gameStatusResponse);
          }
        });
        stomp.subscribe("/topic/split-card-incoming", (message: any) => {
          let splitResponse: SplitResponse = JSON.parse(message.body);
          if (splitResponse.gameUUID == gameUUID) {
            setSplitTurn(true);
            if (splitResponse.leftPlayingCard) {
              setLeftSplitPlayingCard(prev => splitResponse.newEdgeCard);
              setRightSplitPlayingCard(prev => undefined);
            } else {
              setLeftSplitPlayingCard(prev => undefined);
              setRightSplitPlayingCard(prev => splitResponse.newEdgeCard);
            }
          }
        });
        stomp.subscribe("/topic/bet-result", (message: any) => {
          let betResult: BetResult = JSON.parse(message.body);
          if (betResult.uuidOfGame == gameUUID) {
            setMiddlePlayingCard(betResult.middleCard);
            setPotTotal(betResult.potTotal);
            setPlayerList(betResult.playerList);
            setBetResult(betResult);
          }
        });
        stomp.subscribe("/topic/start-turn", (message: any) => {
          let gameUpdateSTartTurnMessage: GameUpdateStartTurn = JSON.parse(
            message.body
          );
          if (gameUpdateSTartTurnMessage.uuid == gameUUID) {
            setSplitTurn(false);
            setLeftSplitPlayingCard(undefined);
            setRightSplitPlayingCard(undefined);
            setPotTotal(gameUpdateSTartTurnMessage.potTotal);
            setLeftPlayingCard(gameUpdateSTartTurnMessage.leftPlayingCard);
            setRightPlayingCard(gameUpdateSTartTurnMessage.rightPlayingCard);
            setMiddlePlayingCard(undefined);
            setMaxBidAllowed(gameUpdateSTartTurnMessage.maxBidAllowed);
            setPlayerList(gameUpdateSTartTurnMessage.playerList);
            setCardsUntilReshuffle(
              gameUpdateSTartTurnMessage.cardsLeftUntilReshuffle
            );
            setBetResult(undefined);
          }
        });
      });
    }

    return () => webSocket.close();
  }, [gameUUID, userId]);

  const handleStartGame = (e: any) => {
    e.preventDefault();
    const confirmStart: boolean = confirm(
      "Are you sure you want to start the game? Has everyone joined?"
    );
    if (confirmStart) {
      axios
        .post("/perform:START_GAME", {
          uuidToStart: gameUUID,
          startGame: true
        })
        .then(response => {
          if (response.status == 200) {
            console.log("Started Game submitted 200");
          }
        });
    }
  };

  const handlePassOfTurn = (e: any) => {
    e.preventDefault();
    console.log(leftSplitPlayingCard);
    if (splitTurn && leftSplitPlayingCard != undefined) {
      axios
        .post("/perform:PASS_SPLIT", {
          uuid: gameUUID,
          userId: userId,
          passSplit: true
        })
        .then(response => {
          if (response.status == 200) {
            console.log("Started Game submitted 200");
          }
        });
    } else {
      axios
        .post("/perform:PASS_TURN", {
          uuidToPass: gameUUID,
          userId: userId,
          passTurn: true,
          splitTurnPass: splitTurn
        })
        .then(response => {});
    }
  };

  const handleBid = (e: any) => {
    e.preventDefault();
    const bidAmount = bidAmountRef.current.value;

    if (splitTurn) {
      axios
        .post("/perform:SPLIT_BET", {
          uuid: gameUUID,
          userId: userId,
          splitBid: true,
          wagerAmount: bidAmount,
          leftCard: leftSplitPlayingCard != undefined,
          rightCard: rightSplitPlayingCard != undefined
        })
        .then(response => {
          if (response.status == 200) {
            setWaitingAcknowledgeResults(true);
          }
        });
    } else {
      if (bidAmount <= maxBidAllowed && bidAmount > 0) {
        axios
          .post("/perform:BET", {
            userBettingId: userId,
            wagerAmount: bidAmount,
            uuidOfGame: gameUUID
          })
          .then(response => {
            if (response.status == 200) {
              setWaitingAcknowledgeResults(true);
            }
          });
      }
    }
  };

  const handleAdvance = (e: any) => {
    e.preventDefault();
    axios
      .post("/perform:ACKNOWLEDGE_RESULTS", {
        acknowledgeResult: true,
        uuid: gameUUID
      })
      .then(response => {
        if (response.status == 200) {
          setWaitingAcknowledgeResults(false);
        }
      });
  };

  const handleSplitAction = (e: any) => {
    axios
      .post("/perform:SPLIT", {
        performSplit: true,
        uuid: gameUUID,
        userSplitting: userId
      })
      .then(response => {
        if (response.status == 200) {
        }
      });
  };

  const isUsersTurn: boolean = useMemo(() => {
    if (playerList != undefined) {
      const players = playerList?.filter(player => player.playersTurn);
      return players[0].userId == userId;
    }
    return false;
  }, [playerList]);

  const leftPlayingCardImg: JSX.Element = useMemo(() => {
    if (leftPlayingCard != undefined) {
      let src = `/img/cards/fronts/${leftPlayingCard.suit.toLowerCase()}_${
        leftPlayingCard.cardValue
      }.png`;
      return (
        <img
          className={classNames({
            blur: rightSplitPlayingCard != undefined && splitTurn
          })}
          src={src}
        />
      );
    }
    return <img src="/img/cards/backs/red2.png" />;
  }, [leftPlayingCard, rightSplitPlayingCard]);

  const leftSplitPlayingCardImg: JSX.Element | null = useMemo(() => {
    if (leftSplitPlayingCard != undefined) {
      let src = `/img/cards/fronts/${leftSplitPlayingCard.suit.toLowerCase()}_${
        leftSplitPlayingCard.cardValue
      }.png`;
      return <img className="split-card left" src={src} />;
    }
    return null;
  }, [splitTurn, leftSplitPlayingCard]);

  const rightPlayingCardImg: JSX.Element = useMemo(() => {
    if (rightPlayingCard != undefined) {
      let src = `/img/cards/fronts/${rightPlayingCard.suit.toLowerCase()}_${
        rightPlayingCard.cardValue
      }.png`;
      return (
        <img
          className={classNames({
            blur: leftSplitPlayingCard != undefined && splitTurn
          })}
          src={src}
        />
      );
    }
    return <img src="/img/cards/backs/red2.png" />;
  }, [rightPlayingCard, leftSplitPlayingCard]);

  const rightSplitPlayingCardImg: JSX.Element | null = useMemo(() => {
    if (rightSplitPlayingCard != undefined) {
      let src = `/img/cards/fronts/${rightSplitPlayingCard.suit.toLowerCase()}_${
        rightSplitPlayingCard.cardValue
      }.png`;
      return <img className="split-card right" src={src} />;
    }
    return null;
  }, [splitTurn, rightSplitPlayingCard]);

  const middlePlayingCardImg: JSX.Element = useMemo(() => {
    if (middlePlayingCard != undefined) {
      let src = `/img/cards/fronts/${middlePlayingCard.suit.toLowerCase()}_${
        middlePlayingCard.cardValue
      }.png`;
      return <img src={src} />;
    }
    return <></>;
  }, [middlePlayingCard]);

  const playersWaitingRows: JSX.Element[] | null = useMemo(() => {
    return playerList?.map((x, index) => (
      <tr className={classNames({ currentTurn: x.playersTurn })} key={index}>
        <td className={classNames({ "my-user": x.userId == userId })}>
          ({x.score}) - {x.displayName}
        </td>
        {/* <td className={"turn-status"}>{x.playersTurn ? "Users Turn" : ""}</td> */}
      </tr>
    ));
  }, [playerList]);

  const gamePotData: JSX.Element = useMemo(() => {
    if (
      potTotal == undefined ||
      playerList == undefined ||
      playerList.length == 0
    )
      return <></>;

    const totalPointsInPlay: number = playerList?.reduce((x, y) => {
      return x + y.score;
    }, 0);
    const percentage = (potTotal / (potTotal + totalPointsInPlay)) * 100;

    return (
      <>
        <div>Pot total: {potTotal}</div>
        <ProgressBar now={percentage} />
      </>
    );
  }, [potTotal, playerList]);

  const cardData: JSX.Element = useMemo(() => {
    if (cardsUntilReshuffle == undefined) return <></>;

    return <>Re-Shuffle in: {cardsUntilReshuffle}</>;
  }, [cardsUntilReshuffle]);

  const controlButtons: JSX.Element = useMemo(() => {
    if (gameStatusResponse != undefined) {
      if (gameStatusResponse.gameStatus == "OPEN") {
        return (
          <Row>
            <Col>
              <Button onClick={handleStartGame}>Start Game</Button>
            </Col>
          </Row>
        );
      } else if (
        isUsersTurn &&
        gameStatusResponse.gameStatus == "IN_SESSION" &&
        !waitingAcknowledgeResults
      ) {
        const betInput: JSX.Element = (
          <form onSubmit={handleBid} id="bet-controls-wrapper">
            <input
              ref={bidAmountRef}
              id="bet-amount-input"
              type={"number"}
              step={1}
              min={1}
              max={maxBidAllowed > potTotal ? potTotal : maxBidAllowed}
              placeholder={maxBidAllowed.toString()}
            />
            <Button type="submit" variant="outline-secondary" id="bidButton">
              Bet
            </Button>
          </form>
        );

        const splitButton: JSX.Element = (
          <Button onClick={handleSplitAction} id="split-start-button">
            Split
          </Button>
        );

        return (
          <Row>
            <Col>
              {leftPlayingCard?.cardValue == rightPlayingCard?.cardValue &&
              !splitTurn
                ? splitButton
                : betInput}
              <Button onClick={handlePassOfTurn}>PASS</Button>
            </Col>
          </Row>
        );
      } else if (betResult != null) {
        const advanceButton: JSX.Element = (
          <Button id="advance-btn" onClick={handleAdvance}>
            Advance
          </Button>
        );

        return (
          <>
            <Row>
              <Col>
                {betResult.wonBet ? "Winner" : "Maybe Next Time..."} (
                {betResult.amountShifted})
              </Col>
            </Row>
            <Row>
              <Col>
                {waitingAcknowledgeResults ? (
                  advanceButton
                ) : (
                  <>..waiting on Better to progress</>
                )}
              </Col>
            </Row>
          </>
        );
      }
    }
    return <></>;
  }, [
    gameStatusResponse,
    playerList,
    maxBidAllowed,
    waitingAcknowledgeResults,
    betResult,
    splitTurn,
    leftSplitPlayingCard,
    rightSplitPlayingCard
  ]);

  return (
    <>
      <main id="gamelobby">
        <Container>
          <Row>
            <Col lg={9}>
              <Card>
                <div id="cardData">{cardData}</div>
                <div id="game-table-wrapper">
                  <table id="game-board">
                    <tbody>
                      <tr id="cards">
                        <td>
                          {leftPlayingCardImg}
                          {leftSplitPlayingCardImg}
                        </td>
                        <td>{middlePlayingCardImg}</td>
                        <td>
                          {rightPlayingCardImg}
                          {rightSplitPlayingCardImg}
                        </td>
                      </tr>
                    </tbody>
                  </table>
                  <div id="control-buttons">{controlButtons}</div>
                </div>
                <div id="game-data-wrapper">
                  <div id="gamePotData">{gamePotData}</div>
                </div>
              </Card>
            </Col>
            <Col lg={3}>
              <Card id="game-status-card">
                Game Status: {gameStatusResponse?.gameStatus.replace("_", " ")}
              </Card>
              <Card id="player-turn-order-card">
                <table id="player-turn-order">
                  <tbody>{playersWaitingRows}</tbody>
                </table>
              </Card>
            </Col>
          </Row>
        </Container>
      </main>
    </>
  );
};
