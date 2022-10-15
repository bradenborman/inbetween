package inbetween.models;

import inbetween.models.enums.CardSuite;
import inbetween.models.enums.CardValue;

public class PlayingCard {

    private String cardId;
    private CardValue cardValue;
    private CardSuite suit;

    public PlayingCard(CardValue cardValue, CardSuite suit) {
        this.cardValue = cardValue;
        this.suit = suit;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public CardValue getCardValue() {
        return cardValue;
    }

    public void setCardValue(CardValue cardValue) {
        this.cardValue = cardValue;
    }

    public CardSuite getSuit() {
        return suit;
    }

    public void setSuit(CardSuite suit) {
        this.suit = suit;
    }
}