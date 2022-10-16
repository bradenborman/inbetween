package inbetween.services;

import inbetween.daos.UserDao;
import inbetween.models.NextTurnMessage;
import inbetween.models.Player;
import inbetween.utilities.NextTurnUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NextTurnMessagingService {

    private static final Logger logger = LoggerFactory.getLogger(NextTurnMessagingService.class);

    private final UserDao userDao;

    public NextTurnMessagingService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void updateNextTurnAndSendMessage(int gameId) {
        logger.info("Updating next turn for gameId: {}", gameId);

        NextTurnMessage nextTurnMessage = new NextTurnMessage();

        List<Player> playerListBefore = userDao.selectPlayersFromGame(gameId);
        List<Player> sortedBefore = NextTurnUtility.sortForNextTurn(playerListBefore);

        if (sortedBefore.size() > 1) {
            logger.info("Updating next turn for user: {}", sortedBefore.get(1).getUserId());
            userDao.updateNextTurnForUser(gameId, sortedBefore.get(1));
        }

        List<Player> playerListAfterUpdate = userDao.selectPlayersFromGame(gameId);
        nextTurnMessage.setPlayerTurnOrder(NextTurnUtility.sortForNextTurn(playerListAfterUpdate));
        nextTurnMessage.setMaxBidAllowed(50);

        //Send Message to everyone
    }


}