package inbetween.models;

public class CardTable {

    private int gameId;

    private PlayingCard leftCardShowing;
    private PlayingCard rightCardShowing;
    private PlayingCard middleCardShowing;

    public CardTable(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public PlayingCard getLeftCardShowing() {
        return leftCardShowing;
    }

    public void setLeftCardShowing(PlayingCard leftCardShowing) {
        this.leftCardShowing = leftCardShowing;
    }

    public PlayingCard getRightCardShowing() {
        return rightCardShowing;
    }

    public void setRightCardShowing(PlayingCard rightCardShowing) {
        this.rightCardShowing = rightCardShowing;
    }

    public PlayingCard getMiddleCardShowing() {
        return middleCardShowing;
    }

    public void setMiddleCardShowing(PlayingCard middleCardShowing) {
        this.middleCardShowing = middleCardShowing;
    }
}
