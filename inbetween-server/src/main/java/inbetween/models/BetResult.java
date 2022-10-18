package inbetween.models;

import java.util.List;

public class BetResult {

    private String uuidOfGame;
    private int amountShifted;
    private boolean wonBet;
    private boolean penaltyApplied;
    private int potTotal;
    private List<Player> playerList;

    private PlayingCard middleCard;

    public String getUuidOfGame() {
        return uuidOfGame;
    }

    public void setUuidOfGame(String uuidOfGame) {
        this.uuidOfGame = uuidOfGame;
    }

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

    public int getPotTotal() {
        return potTotal;
    }

    public void setPotTotal(int potTotal) {
        this.potTotal = potTotal;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
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