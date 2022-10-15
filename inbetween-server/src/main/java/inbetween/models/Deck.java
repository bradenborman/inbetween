package inbetween.models;

import java.util.List;

public class Deck {

    private List<PlayingCard> shuffledDeck;

    public Deck(List<PlayingCard> shuffledDeck) {
        this.shuffledDeck = shuffledDeck;
    }

    public Deck() {
    }

    public List<PlayingCard> getShuffledDeck() {
        return shuffledDeck;
    }

}