package inbetween.models.enums;

import inbetween.models.actions.SplitBetActionRequest;

public enum PlayingCardColumnName {

    LEFT("left_card"),
    MIDDLE("middle_card"),
    RIGHT("right_card"),
    SPLIT_CARD("split_card");

    private final String columnName;

    PlayingCardColumnName(String columnName) {
        this.columnName = columnName;
    }

    public static PlayingCardColumnName deriveCardToUse(SplitBetActionRequest splitBetActionRequest) {
        if (splitBetActionRequest.isLeftCard())
            return LEFT;
        else if (splitBetActionRequest.isRightCard())
            return RIGHT;

        throw new RuntimeException("Failed to determine which edge card to use");
    }

    public String getColumnName() {
        return columnName;
    }
}