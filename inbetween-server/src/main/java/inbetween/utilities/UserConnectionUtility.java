package inbetween.utilities;

import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

public class UserConnectionUtility {

    private UserConnectionUtility() {
        throw new IllegalStateException("Utility Class only");
    }


    public static String findUserFromString(String path) {
        return path.replace("/topic/user-connected/", "");
    }

    public static String findSimpSessionId(AbstractSubProtocolEvent event) {
        return (String) event.getMessage().getHeaders().get("simpSessionId");
    }

}