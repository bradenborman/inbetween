package inbetween.config;

import inbetween.actions.PassTurnGameAction;
import inbetween.daos.GameDao;
import inbetween.daos.UserDao;
import inbetween.models.Player;
import inbetween.models.actions.PassTurnActionRequest;
import inbetween.models.enums.GameStatus;
import inbetween.services.NextTurnMessagingService;
import inbetween.utilities.UserConnectionUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Controller
public class UsersOnlineTracker {

    private static final Logger logger = LoggerFactory.getLogger(UsersOnlineTracker.class);

    private final UserDao userDao;
    private final GameDao gameDao;
    private final PassTurnGameAction passTurnGameAction;
    private final NextTurnMessagingService nextTurnMessagingService;

    public UsersOnlineTracker(UserDao userDao, GameDao gameDao, PassTurnGameAction passTurnGameAction,
                              NextTurnMessagingService nextTurnMessagingService) {
        this.userDao = userDao;
        this.gameDao = gameDao;
        this.passTurnGameAction = passTurnGameAction;
        this.nextTurnMessagingService = nextTurnMessagingService;
    }

    @EventListener(SessionSubscribeEvent.class)
    private void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        String pathConnected = (String) event.getMessage().getHeaders().get("simpDestination");
        if (pathConnected != null && pathConnected.contains("/user-connected")) {
            String userId = UserConnectionUtility.findUserFromString(pathConnected);
            String simpSessionId = UserConnectionUtility.findSimpSessionId(event);
            logger.info("User {} joined game: {}", userId, simpSessionId);

            userDao.updatePlayersSimpSessionId(userId, simpSessionId);
        }
    }

    @EventListener(SessionDisconnectEvent.class)
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionIdClosed = UserConnectionUtility.findSimpSessionId(event);
        boolean wasUpdated = userDao.updateUserLeftBySessionId(sessionIdClosed);
        if (wasUpdated) {
            logger.info("User left {}", sessionIdClosed);
            Player player = userDao.fillUserDetails(userDao.findUserIDBySessionId(sessionIdClosed));

            String uuid = gameDao.getUUIDByGameId(player.getGameJoined());
            GameStatus gameStatus = gameDao.getGameStatusByUUID(uuid);

            if (player.isPlayersTurn() && gameStatus == GameStatus.IN_SESSION) {
                //pass turn if players turn

                PassTurnActionRequest passAction = new PassTurnActionRequest();
                passAction.setUserId(String.valueOf(player.getUserId()));
                passAction.setUuidToPass(uuid);
                passTurnGameAction.perform(passAction);
            }

            //ONLY update the next turn to the next person - do not perform the inGame pass turn Action
            if (player.isPlayersTurn() && gameStatus == GameStatus.OPEN) {
                nextTurnMessagingService.updateNextTurn(gameDao.getGameIdByUUID(uuid));
            }

            //clean up game if last player left
            int remainingPlaying = userDao.countPlayersActiveInLobby(player.getGameJoined());
            if (remainingPlaying <= 0) {
                //TODO TIME TO CLEAN UP GAME
                logger.info("TIME TO CLEAN UP GAME");
            }

        }
    }


}