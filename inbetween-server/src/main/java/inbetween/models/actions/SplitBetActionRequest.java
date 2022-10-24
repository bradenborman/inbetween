package inbetween.models.actions;

public class SplitBetActionRequest extends ActionRequest {

    private String uuid;
    private String userId;
    private boolean splitBid;
    private int wagerAmount;
    private boolean leftCard;
    private boolean rightCard;

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

    public boolean isSplitBid() {
        return splitBid;
    }

    public void setSplitBid(boolean splitBid) {
        this.splitBid = splitBid;
    }

    public int getWagerAmount() {
        return wagerAmount;
    }

    public void setWagerAmount(int wagerAmount) {
        this.wagerAmount = wagerAmount;
    }

    public boolean isLeftCard() {
        return leftCard;
    }

    public void setLeftCard(boolean leftCard) {
        this.leftCard = leftCard;
    }

    public boolean isRightCard() {
        return rightCard;
    }

    public void setRightCard(boolean rightCard) {
        this.rightCard = rightCard;
    }
}