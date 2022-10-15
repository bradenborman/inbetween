package inbetween.utilities;

import inbetween.models.Deck;
import inbetween.models.PlayingCard;
import inbetween.models.enums.CardSuite;
import inbetween.models.enums.CardValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckUtility {

    public static Deck initializeNewDeck() {
        List<PlayingCard> playingCards = new ArrayList<>();
        for (CardValue cardValue : CardValue.values())
            for (CardSuite suite : CardSuite.values())
                playingCards.add(new PlayingCard(cardValue, suite));

        Collections.shuffle(playingCards);

        return new Deck(playingCards);
    }

}