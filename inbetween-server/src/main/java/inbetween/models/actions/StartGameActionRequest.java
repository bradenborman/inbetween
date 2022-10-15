package inbetween.models.actions;

public class StartGameActionRequest extends ActionRequest {

    private boolean startGame;

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }
}