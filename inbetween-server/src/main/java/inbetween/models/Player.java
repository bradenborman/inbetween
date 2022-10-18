package inbetween.models;

import inbetween.models.enums.UserRole;

public class Player {

    private int userId;
    private String displayName;
    private boolean isPlayersTurn;
    private UserRole userRole;
    private int score;
    private int gameJoined;

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

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGameJoined() {
        return gameJoined;
    }

    public void setGameJoined(int gameJoined) {
        this.gameJoined = gameJoined;
    }
}