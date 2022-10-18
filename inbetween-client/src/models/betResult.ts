import PlayingCard from "./playingCard";

export default interface BetResult {
  uuidOfGame: string;
  amountShifted: number;
  wonBet: boolean;
  penaltyApplied: boolean;
  middleCard: PlayingCard;
  potTotal: number;
}
