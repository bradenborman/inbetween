package inbetween.models;

import java.util.List;

public class NextTurnMessage {

    private List<Player> playerTurnOrder;
    private int maxBidAllowed;

    public List<Player> getPlayerTurnOrder() {
        return playerTurnOrder;
    }

    public void setPlayerTurnOrder(List<Player> playerTurnOrder) {
        this.playerTurnOrder = playerTurnOrder;
    }

    public int getMaxBidAllowed() {
        return maxBidAllowed;
    }

    public void setMaxBidAllowed(int maxBidAllowed) {
        this.maxBidAllowed = maxBidAllowed;
    }
}