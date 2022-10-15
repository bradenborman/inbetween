package inbetween.models.actions;

public class PassTurnActionRequest extends ActionRequest {

    private boolean passTurn;

    public boolean isPassTurn() {
        return passTurn;
    }

    public void setPassTurn(boolean passTurn) {
        this.passTurn = passTurn;
    }
}
