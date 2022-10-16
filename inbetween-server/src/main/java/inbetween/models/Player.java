package inbetween.models;

public class Player {

    private int userId;
    private String displayName;
    private boolean isPlayersTurn;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isPlayersTurn() {
        return isPlayersTurn;
    }

    public void setPlayersTurn(boolean playersTurn) {
        isPlayersTurn = playersTurn;
    }
}