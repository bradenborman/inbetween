package inbetween.config;

import inbetween.daos.UserDao;
import inbetween.models.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketBroker {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketBroker.class);


    private final UserDao userDao;

    public WebSocketBroker(UserDao userDao) {
        this.userDao = userDao;
    }

    @MessageMapping("/joined")
    @SendTo("/topic/user-joined-game")
    public Player greeting(String userIdJoined) {
        logger.info("User joined game: {}", userIdJoined);
        userDao.updateStatusOnline(userIdJoined);
        return userDao.fillUserDetails(userIdJoined);
    }


}