package inbetween.models.enums;

public enum PlayingCardColumnName {

    LEFT("left_card"),
    MIDDLE("middle_card"),
    RIGHT("right_card");

    private final String columnName;

    PlayingCardColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}