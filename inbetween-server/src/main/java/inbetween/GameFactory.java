package inbetween;

import inbetween.daos.CardDao;
import inbetween.models.CardTable;
import inbetween.models.Deck;
import inbetween.models.PlayingCard;
import inbetween.models.enums.CardSuite;
import inbetween.models.enums.CardValue;
import inbetween.models.enums.PlayingCardColumnName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class GameFactory {

    private static final Logger logger = LoggerFactory.getLogger(GameFactory.class);


    private final CardDao cardDao;

    public GameFactory(CardDao cardDao) {
        this.cardDao = cardDao;
    }

    public int createNewGame() {

        int gameId = cardDao.initNewGame();
        cardDao.insertDeck(gameId, initializeNewDeck());
        cardDao.initGameTable(gameId);
        return gameId;
    }

    //TODO part time
    @PostConstruct
    public void run() {
        int gameId = createNewGame();
    }

    public CardTable getCardTableDetails(int gameId) {
        CardTable cardTable = new CardTable(gameId);

        cardTable.setLeftCardShowing(cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.LEFT));
        cardTable.setRightCardShowing(cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.RIGHT));
        cardTable.setMiddleCardShowing(cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.MIDDLE));

        return cardTable;
    }

    private Deck initializeNewDeck() {
        List<PlayingCard> playingCards = new ArrayList<>();
        for (CardValue cardValue : CardValue.values())
            for (CardSuite suite : CardSuite.values())
                playingCards.add(new PlayingCard(cardValue, suite));

        Collections.shuffle(playingCards);

        return new Deck(playingCards);
    }


}