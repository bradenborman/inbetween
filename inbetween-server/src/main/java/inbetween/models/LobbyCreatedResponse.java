package inbetween.models;

public class LobbyCreatedResponse {

    private int gameId;
    private String uuid;
    private int userPlayingOnScreenId;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getUserPlayingOnScreenId() {
        return userPlayingOnScreenId;
    }

    public void setUserPlayingOnScreenId(int userPlayingOnScreenId) {
        this.userPlayingOnScreenId = userPlayingOnScreenId;
    }
}
