package inbetween.models.actions;

import inbetween.models.enums.UserRole;

public class CreateLobbyActionRequest extends ActionRequest {

    private String displayName;
    private UserRole userRole;
    private String lobbyName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }
}
