package inbetween.models;

public class SplitResponse {

    private String gameUUID;
    private PlayingCard newEdgeCard;
    private boolean leftPlayingCard;

    public String getGameUUID() {
        return gameUUID;
    }

    public void setGameUUID(String gameUUID) {
        this.gameUUID = gameUUID;
    }

    public PlayingCard getNewEdgeCard() {
        return newEdgeCard;
    }

    public void setNewEdgeCard(PlayingCard newEdgeCard) {
        this.newEdgeCard = newEdgeCard;
    }

    public boolean isLeftPlayingCard() {
        return leftPlayingCard;
    }

    public void setLeftPlayingCard(boolean leftPlayingCard) {
        this.leftPlayingCard = leftPlayingCard;
    }

}