package inbetween.models.actions;

import inbetween.models.enums.UserRole;

public class JoinLobbyActionRequest extends ActionRequest {

    private String displayName;
    private UserRole playerRole;
    private boolean joinGame;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UserRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(UserRole playerRole) {
        this.playerRole = playerRole;
    }

    public boolean isJoinGame() {
        return joinGame;
    }

    public void setJoinGame(boolean joinGame) {
        this.joinGame = joinGame;
    }

}
