package inbetween.utilities;

import inbetween.models.BetResult;
import inbetween.models.PlayingCard;
import inbetween.models.enums.CardSuite;
import inbetween.models.enums.CardValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class BetResultUtilityTest {

    @Test
    void deriveBetResultWinningBetTest() {
        BetResult results = BetResultUtility.deriveBetResult(
                new PlayingCard(CardValue.FIVE, CardSuite.DIAMONDS),
                new PlayingCard(CardValue.JACK, CardSuite.SPADES),
                new PlayingCard(CardValue.EIGHT, CardSuite.HEARTS),
                50);

        Assertions.assertTrue(results.isWonBet());
        Assertions.assertEquals(100, results.getAmountShifted());
    }

    @Test
    void deriveBetResultLosingBetTest() {
        BetResult results = BetResultUtility.deriveBetResult(
                new PlayingCard(CardValue.TWO, CardSuite.HEARTS),
                new PlayingCard(CardValue.JACK, CardSuite.CLUBS),
                new PlayingCard(CardValue.QUEEN, CardSuite.SPADES),
                50);

        Assertions.assertFalse(results.isWonBet());
        Assertions.assertEquals(-50, results.getAmountShifted());
    }

    @Test
    void deriveBetResultEdgeHitLossBetTest() {
        BetResult results = BetResultUtility.deriveBetResult(
                new PlayingCard(CardValue.TWO, CardSuite.HEARTS),
                new PlayingCard(CardValue.QUEEN, CardSuite.CLUBS),
                new PlayingCard(CardValue.QUEEN, CardSuite.SPADES),
                50);

        Assertions.assertFalse(results.isWonBet());
        Assertions.assertTrue(results.isPenaltyApplied());
        Assertions.assertEquals(-100, results.getAmountShifted());
    }

}