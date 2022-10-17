import Player from "./player";

export default interface JoinedGameResponse {
  uuid: string;
  playerIdCreated: string;
  playersJoined: Player[];
}
