package inbetween.services;

import inbetween.daos.CardDao;
import inbetween.daos.GameDao;
import inbetween.daos.UserDao;
import inbetween.models.GameUpdateStartTurn;
import inbetween.models.Player;
import inbetween.models.enums.PlayingCardColumnName;
import inbetween.utilities.NextTurnUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NextTurnMessagingService {

    private static final Logger logger = LoggerFactory.getLogger(NextTurnMessagingService.class);

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameDao gameDao;
    private final UserDao userDao;
    private final CardDao cardDao;

    public NextTurnMessagingService(SimpMessagingTemplate simpMessagingTemplate, GameDao gameDao,
                                    UserDao userDao, CardDao cardDao) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.gameDao = gameDao;
        this.userDao = userDao;
        this.cardDao = cardDao;
    }

    public void updateNextTurnAndSendMessage(int gameId, boolean updateTurn) {

        if (updateTurn) {
            logger.info("Updating next turn for gameId: {}", gameId);
            List<Player> playerListBefore = userDao.selectPlayersFromGame(gameId);
            List<Player> sortedBefore = NextTurnUtility.sortForNextTurn(playerListBefore);
            if (sortedBefore.size() > 1) {
                logger.info("Updating next turn for user: {}", sortedBefore.get(1).getUserId());
                userDao.updateNextTurnForUser(gameId, sortedBefore.get(1));
            }
        }

        //Populate message
        GameUpdateStartTurn gameUpdateStartTurn = new GameUpdateStartTurn();
        gameUpdateStartTurn.setPlayerList(userDao.selectPlayersFromGame(gameId));
        gameUpdateStartTurn.setUuid(gameDao.getUUIDByGameId(gameId));
        gameUpdateStartTurn.setLeftPlayingCard(cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.LEFT));
        gameUpdateStartTurn.setRightPlayingCard(cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.RIGHT));
        gameUpdateStartTurn.setCardsLeftUntilReshuffle(cardDao.countUnitlNextShuffle(gameId));
        gameUpdateStartTurn.setPotTotal(gameDao.countPotTotalInPlay(gameId));
        gameUpdateStartTurn.setMaxBidAllowed(50); //TODO set max

        //Send Message to everyone
        simpMessagingTemplate.convertAndSend("/topic/start-turn", gameUpdateStartTurn);
    }


}