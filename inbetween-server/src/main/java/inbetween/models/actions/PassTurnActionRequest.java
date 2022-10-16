package inbetween.models.actions;

public class PassTurnActionRequest extends ActionRequest {

    private String userId;
    private boolean passTurn;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isPassTurn() {
        return passTurn;
    }

    public void setPassTurn(boolean passTurn) {
        this.passTurn = passTurn;
    }
}
