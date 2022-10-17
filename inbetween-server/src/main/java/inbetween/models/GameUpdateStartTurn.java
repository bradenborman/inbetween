package inbetween.models;

import java.util.List;

public class GameUpdateStartTurn {

    private String uuid;
    private List<Player> playerList;
    private int potTotal;
    private PlayingCard leftPlayingCard;
    private PlayingCard rightPlayingCard;
    private int maxBidAllowed;
    private int cardsLeftUntilReshuffle;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public int getPotTotal() {
        return potTotal;
    }

    public void setPotTotal(int potTotal) {
        this.potTotal = potTotal;
    }

    public PlayingCard getLeftPlayingCard() {
        return leftPlayingCard;
    }

    public void setLeftPlayingCard(PlayingCard leftPlayingCard) {
        this.leftPlayingCard = leftPlayingCard;
    }

    public PlayingCard getRightPlayingCard() {
        return rightPlayingCard;
    }

    public void setRightPlayingCard(PlayingCard rightPlayingCard) {
        this.rightPlayingCard = rightPlayingCard;
    }

    public int getMaxBidAllowed() {
        return maxBidAllowed;
    }

    public void setMaxBidAllowed(int maxBidAllowed) {
        this.maxBidAllowed = maxBidAllowed;
    }

    public int getCardsLeftUntilReshuffle() {
        return cardsLeftUntilReshuffle;
    }

    public void setCardsLeftUntilReshuffle(int cardsLeftUntilReshuffle) {
        this.cardsLeftUntilReshuffle = cardsLeftUntilReshuffle;
    }
}
