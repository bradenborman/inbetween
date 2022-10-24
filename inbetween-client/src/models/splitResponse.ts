import PlayingCard from "./playingCard";

export default interface SplitResponse {
  gameUUID: string;
  newEdgeCard: PlayingCard;
  leftPlayingCard: boolean;
}
