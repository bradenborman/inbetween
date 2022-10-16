package inbetween.services;

import inbetween.daos.CardDao;
import inbetween.daos.UserDao;
import inbetween.models.BetResult;
import inbetween.models.PlayingCard;
import inbetween.models.actions.BetActionRequest;
import inbetween.models.enums.PlayingCardColumnName;
import inbetween.utilities.BetResultUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

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
        Stream.of(PlayingCardColumnName.LEFT, PlayingCardColumnName.RIGHT)
                .forEach(columnName -> cardDao.loadCardToBoard(gameId, columnName, cardDao.drawNextCard(gameId)));
    }

    public BetResult performNewBet(BetActionRequest betActionRequest) {
        int gameId = betActionRequest.getGameId();

        PlayingCard playingCardL = cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.LEFT);
        PlayingCard playingCardR = cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.RIGHT);

        PlayingCard playingCardM = cardDao.drawNextCard(gameId);
        cardDao.loadCardToBoard(gameId, PlayingCardColumnName.MIDDLE, playingCardM);

        BetResult betResult = BetResultUtility.deriveBetResult(
                playingCardL, playingCardR, playingCardM, betActionRequest.getWagerAmount()
        );

        logger.info("Bet Result: {}", betResult.toString());

        return betResult;

    }

    public void clearMiddleCard(int gameId) {
        cardDao.clearCard(gameId, PlayingCardColumnName.MIDDLE);
    }
}