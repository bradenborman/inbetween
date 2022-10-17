package inbetween.models;

import inbetween.models.enums.UserRole;

public class JoinedGameResponse {

    private String uuid;
    private int userPlayingOnScreenId;
    private String displayName;
    private UserRole playerRole;

    public JoinedGameResponse(String uuid, int userPlayingOnScreenId, String displayName, UserRole playerRole) {
        this.uuid = uuid;
        this.userPlayingOnScreenId = userPlayingOnScreenId;
        this.displayName = displayName;
        this.playerRole = playerRole;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getUserPlayingOnScreenId() {
        return userPlayingOnScreenId;
    }

    public void setUserPlayingOnScreenId(int userPlayingOnScreenId) {
        this.userPlayingOnScreenId = userPlayingOnScreenId;
    }

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
}
