package inbetween.models.actions;

public class SplitActionRequest extends ActionRequest {

    private String uuid;
    private boolean performSplit;
    private String userSplitting;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isPerformSplit() {
        return performSplit;
    }

    public void setPerformSplit(boolean performSplit) {
        this.performSplit = performSplit;
    }

    public String getUserSplitting() {
        return userSplitting;
    }

    public void setUserSplitting(String userSplitting) {
        this.userSplitting = userSplitting;
    }

}