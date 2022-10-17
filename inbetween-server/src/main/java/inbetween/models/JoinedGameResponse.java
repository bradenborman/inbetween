package inbetween.models;

import java.util.List;

public class JoinedGameResponse {

    private String uuid;
    public int playerIdCreated;
    private List<Player> playersJoined;

    public JoinedGameResponse(String uuid, int playerIdCreated, List<Player> playersJoined) {
        this.uuid = uuid;
        this.playerIdCreated = playerIdCreated;
        this.playersJoined = playersJoined;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getPlayerIdCreated() {
        return playerIdCreated;
    }

    public void setPlayerIdCreated(int playerIdCreated) {
        this.playerIdCreated = playerIdCreated;
    }

    public List<Player> getPlayersJoined() {
        return playersJoined;
    }

    public void setPlayersJoined(List<Player> playersJoined) {
        this.playersJoined = playersJoined;
    }
}
