package inbetween.models;

public class BetResult {

    private int amountShifted;
    private boolean wonBet;
    private boolean penaltyApplied;

    private PlayingCard middleCard;

    public int getAmountShifted() {
        return amountShifted;
    }

    public void setAmountShifted(int amountShifted) {
        this.amountShifted = amountShifted;
    }

    public boolean isWonBet() {
        return wonBet;
    }

    public void setWonBet(boolean wonBet) {
        this.wonBet = wonBet;
    }

    public boolean isPenaltyApplied() {
        return penaltyApplied;
    }

    public void setPenaltyApplied(boolean penaltyApplied) {
        this.penaltyApplied = penaltyApplied;
    }

    public PlayingCard getMiddleCard() {
        return middleCard;
    }

    public void setMiddleCard(PlayingCard middleCard) {
        this.middleCard = middleCard;
    }

    @Override
    public String toString() {
        return "BetResult{" +
                "amountShifted=" + amountShifted +
                ", wonBet=" + wonBet +
                ", penaltyApplied=" + penaltyApplied +
                ", middleCard=" + middleCard +
                '}';
    }
}