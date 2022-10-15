package inbetween.models.actions;

public class AcknowledgeResultActionRequest extends ActionRequest {

    private boolean acknowledgeResult;

    public boolean isAcknowledgeResult() {
        return acknowledgeResult;
    }

    public void setAcknowledgeResult(boolean acknowledgeResult) {
        this.acknowledgeResult = acknowledgeResult;
    }
}
