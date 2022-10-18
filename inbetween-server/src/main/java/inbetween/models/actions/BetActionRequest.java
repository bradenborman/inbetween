package inbetween.models.actions;

public class BetActionRequest extends ActionRequest {

    private String uuidOfGame;
    private String userBettingId;
    private int wagerAmount;

    public String getUuidOfGame() {
        return uuidOfGame;
    }

    public void setUuidOfGame(String uuidOfGame) {
        this.uuidOfGame = uuidOfGame;
    }

    public String getUserBettingId() {
        return userBettingId;
    }

    public void setUserBettingId(String userBettingId) {
        this.userBettingId = userBettingId;
    }

    public int getWagerAmount() {
        return wagerAmount;
    }

    public void setWagerAmount(int wagerAmount) {
        this.wagerAmount = wagerAmount;
    }

}