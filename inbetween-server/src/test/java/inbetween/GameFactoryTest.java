package inbetween;

import inbetween.daos.CardDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


class GameFactoryTest {

    private GameFactory gameFactory;

    @Mock
    CardDao cardDao;

    @BeforeEach
    void setUp() {
        gameFactory = new GameFactory(cardDao);
    }


    @Test
    void createNewGameTest() {
        Assertions.assertDoesNotThrow(() ->
                gameFactory.createNewGame()
        );
    }

}