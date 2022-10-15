package inbetween;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameFactoryIntTest {

    @Autowired
    GameFactory gameFactory;


    @Test
    void createNewGameTest() {
        gameFactory.createNewGame();
        System.out.println("test");
    }
}