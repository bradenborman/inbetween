import Player from "./player";
import PlayingCard from "./playingCard";

export default interface GameUpdateStartTurn {
  uuid: string;
  playerList: Player[];
  potTotal: number;
  leftPlayingCard: PlayingCard;
  rightPlayingCard: PlayingCard;
  maxBidAllowed: number;
  cardsLeftUntilReshuffle: number;
}
