import PlayingCard from "./playingCard";
import Player from "./player";

export default interface BetResult {
  uuidOfGame: string;
  amountShifted: number;
  wonBet: boolean;
  penaltyApplied: boolean;
  middleCard: PlayingCard;
  potTotal: number;
  playerList: Player[];
}
