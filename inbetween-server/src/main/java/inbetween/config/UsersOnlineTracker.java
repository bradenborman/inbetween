package inbetween.config;

import inbetween.actions.PassTurnGameAction;
import inbetween.daos.GameDao;
import inbetween.daos.UserDao;
import inbetween.models.Player;
import inbetween.models.actions.PassTurnActionRequest;
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

    public UsersOnlineTracker(UserDao userDao, GameDao gameDao, PassTurnGameAction passTurnGameAction) {
        this.userDao = userDao;
        this.gameDao = gameDao;
        this.passTurnGameAction = passTurnGameAction;
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

            if (player.isPlayersTurn()) {
                //pass turn if players turn
                String uuid = gameDao.getUUIDByGameId(player.getGameJoined());
                PassTurnActionRequest passAction = new PassTurnActionRequest();
                passAction.setUserId(String.valueOf(player.getUserId()));
                passAction.setUuidToPass(uuid);
                passTurnGameAction.perform(passAction);
            }
        }
    }


}