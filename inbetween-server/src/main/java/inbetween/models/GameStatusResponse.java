package inbetween.models;

import inbetween.models.enums.GameStatus;

public class GameStatusResponse {

    private String uuid;
    private GameStatus gameStatus;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
