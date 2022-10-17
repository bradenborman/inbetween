package inbetween.services;

import inbetween.daos.UserDao;
import inbetween.models.GameUpdateStartTurn;
import inbetween.models.Player;
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
    private final GameService gameService;
    private final UserDao userDao;

    public NextTurnMessagingService(SimpMessagingTemplate simpMessagingTemplate, GameService gameService, UserDao userDao) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.gameService = gameService;
        this.userDao = userDao;
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
        String uuid = gameService.getUUIDByGameId(gameId);
        GameUpdateStartTurn gameUpdateStartTurn = gameService.getLatestStartOfTurnUpdateByUUID(uuid);

        logger.info("Sending Start of new turn update..");

        //Send Message to everyone
        simpMessagingTemplate.convertAndSend("/topic/start-turn", gameUpdateStartTurn);
    }


}