package inbetween.models;

public class LobbyCreatedResponse {

    private int gameId;
    private int userPlayingOnScreenId;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getUserPlayingOnScreenId() {
        return userPlayingOnScreenId;
    }

    public void setUserPlayingOnScreenId(int userPlayingOnScreenId) {
        this.userPlayingOnScreenId = userPlayingOnScreenId;
    }
}
