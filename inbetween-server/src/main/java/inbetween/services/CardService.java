package inbetween.services;

import inbetween.daos.CardDao;
import inbetween.daos.UserDao;
import inbetween.models.BetResult;
import inbetween.models.PlayingCard;
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

    public BetResult performNewBet(int gameId, int wagerAmount) {
        PlayingCard playingCardL = cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.LEFT);
        PlayingCard playingCardR = cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.RIGHT);

        return performNewBet(gameId, wagerAmount, playingCardL, playingCardR);
    }

    public BetResult performNewSplitBet(int gameId, int wagerAmount, PlayingCardColumnName edgeColumn) {
        PlayingCard playingCard1 = cardDao.selectCardShowingInGame(gameId, edgeColumn);
        PlayingCard playingCard2 = cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.SPLIT_CARD);

        return performNewBet(gameId, wagerAmount, playingCard1, playingCard2);
    }

    public BetResult performNewBet(int gameId, int wagerAmount, PlayingCard playingCardL, PlayingCard playingCardR) {
        PlayingCard playingCardM = drawNextCard(gameId);
        cardDao.loadCardToBoard(gameId, PlayingCardColumnName.MIDDLE, playingCardM);

        return BetResultUtility.deriveBetResult(
                playingCardL, playingCardR, playingCardM, wagerAmount
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

    public void clearSplitCard(int gameId) {
        cardDao.clearCard(gameId, PlayingCardColumnName.SPLIT_CARD);
    }
}