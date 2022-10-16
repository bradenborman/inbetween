package inbetween.utilities;

import inbetween.models.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class NextTurnUtilityTest {


    List<Player> fromDBMockList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Player player1 = new Player();
        player1.setPlayersTurn(false);
        player1.setDisplayName("Brent");
        player1.setUserId(1);

        Player player2 = new Player();
        player2.setPlayersTurn(false);
        player2.setDisplayName("Keen");
        player2.setUserId(2);

        Player player3 = new Player();
        player3.setPlayersTurn(true);
        player3.setDisplayName("Jeek");
        player3.setUserId(3);

        Player player4 = new Player();
        player4.setPlayersTurn(false);
        player4.setDisplayName("Cody");
        player4.setUserId(4);

        Player Player5 = new Player();
        Player5.setPlayersTurn(false);
        Player5.setDisplayName("Cori");
        Player5.setUserId(5);

        fromDBMockList.add(player1);
        fromDBMockList.add(player2);
        fromDBMockList.add(player3);
        fromDBMockList.add(player4);
        fromDBMockList.add(Player5);
    }

    @Test
    void sortForNextTurnTest() {

        List<Player> sorted = NextTurnUtility.sortForNextTurn(fromDBMockList);

        assert sorted != null;
        Assertions.assertEquals(3, sorted.get(0).getUserId());
        Assertions.assertEquals(5, sorted.size());
        Assertions.assertEquals(2, sorted.get(4).getUserId());
    }
}