package inbetween.models.actions;

public class PassTurnActionRequest extends ActionRequest {

    private String userId;
    private String uuidToPass;
    private boolean passTurn;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUuidToPass() {
        return uuidToPass;
    }

    public void setUuidToPass(String uuidToPass) {
        this.uuidToPass = uuidToPass;
    }

    public boolean isPassTurn() {
        return passTurn;
    }

    public void setPassTurn(boolean passTurn) {
        this.passTurn = passTurn;
    }

}