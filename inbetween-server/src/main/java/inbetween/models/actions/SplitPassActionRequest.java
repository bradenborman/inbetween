package inbetween.models.actions;

public class SplitPassActionRequest extends ActionRequest {

    private String uuid;
    private String userId;
    private boolean passSplit;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isPassSplit() {
        return passSplit;
    }

    public void setPassSplit(boolean passSplit) {
        this.passSplit = passSplit;
    }

}