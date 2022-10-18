package inbetween.services;

import inbetween.daos.CardDao;
import inbetween.daos.UserDao;
import inbetween.models.BetResult;
import inbetween.models.PlayingCard;
import inbetween.models.actions.BetActionRequest;
import inbetween.models.enums.PlayingCardColumnName;
import inbetween.utilities.BetResultUtility;
import inbetween.utilities.DeckUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    private final UserDao userDao;
    private final CardDao cardDao;

    public CardService(UserDao userDao, CardDao cardDao) {
        this.userDao = userDao;
        this.cardDao = cardDao;
    }

    public void revealTwoNewSideCards(int gameId) {
        cardDao.loadCardToBoard(gameId, PlayingCardColumnName.LEFT, drawNextCard(gameId));
        cardDao.loadCardToBoard(gameId, PlayingCardColumnName.RIGHT, drawNextCard(gameId));
    }

    public BetResult performNewBet(BetActionRequest betActionRequest) {
        int gameId = betActionRequest.getGameId();

        PlayingCard playingCardL = cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.LEFT);
        PlayingCard playingCardR = cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.RIGHT);

        PlayingCard playingCardM = drawNextCard(gameId);
        cardDao.loadCardToBoard(gameId, PlayingCardColumnName.MIDDLE, playingCardM);

        return BetResultUtility.deriveBetResult(
                playingCardL, playingCardR, playingCardM, betActionRequest.getWagerAmount()
        );
    }

    private PlayingCard drawNextCard(int gameId) {
        PlayingCard playingCard = cardDao.drawNextCard(gameId);

        if (cardDao.countUnitlNextShuffle(gameId) == 0)
            cardDao.insertDeck(gameId, DeckUtility.initializeNewDeck());

        return playingCard;
    }


    public void clearMiddleCard(int gameId) {
        cardDao.clearCard(gameId, PlayingCardColumnName.MIDDLE);
    }
}