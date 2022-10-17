package inbetween.config;

import inbetween.daos.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketBroker {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketBroker.class);

    private final UserDao userDao;

    public WebSocketBroker(UserDao userDao) {
        this.userDao = userDao;
    }


}