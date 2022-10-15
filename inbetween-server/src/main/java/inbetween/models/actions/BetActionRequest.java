package inbetween.models.actions;

public class BetActionRequest extends ActionRequest {

    private String userBetting;
    private int wagerAmount;

    public String getUserBetting() {
        return userBetting;
    }

    public void setUserBetting(String userBetting) {
        this.userBetting = userBetting;
    }

    public int getWagerAmount() {
        return wagerAmount;
    }

    public void setWagerAmount(int wagerAmount) {
        this.wagerAmount = wagerAmount;
    }

}