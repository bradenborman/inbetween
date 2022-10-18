package inbetween.models.actions;

public class AcknowledgeResultActionRequest extends ActionRequest {

    private String uuid;
    private boolean acknowledgeResult;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isAcknowledgeResult() {
        return acknowledgeResult;
    }

    public void setAcknowledgeResult(boolean acknowledgeResult) {
        this.acknowledgeResult = acknowledgeResult;
    }
}
