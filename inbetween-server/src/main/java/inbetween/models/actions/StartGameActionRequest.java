package inbetween.models.actions;

public class StartGameActionRequest extends ActionRequest {

    private String uuidToStart;
    private boolean startGame;

    public String getUuidToStart() {
        return uuidToStart;
    }

    public void setUuidToStart(String uuidToStart) {
        this.uuidToStart = uuidToStart;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }
}