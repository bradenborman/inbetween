package inbetween.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserConnectionUtilityTest {


    @Test
    void findUserFromStringTest() {
        String results = UserConnectionUtility.findUserFromString("/topic/user-connected/1");
        Assertions.assertEquals("1", results);
    }
}