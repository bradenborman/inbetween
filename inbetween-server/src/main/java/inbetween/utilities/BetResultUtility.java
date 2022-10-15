package inbetween.utilities;

import inbetween.models.BetResult;
import inbetween.models.PlayingCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetResultUtility {

    private static final Logger logger = LoggerFactory.getLogger(BetResultUtility.class);

    private BetResultUtility() {
        throw new IllegalStateException("Utility class");
    }

    public static BetResult deriveBetResult(PlayingCard left, PlayingCard right, PlayingCard middle, int wagerAmount) {

        int leftValue = left.getCardValue().getValue();
        int middleValue = middle.getCardValue().getValue();
        int rightValue = right.getCardValue().getValue();

        BetResult result = new BetResult();
        result.setMiddleCard(middle);

        if (leftValue < middleValue && rightValue > middleValue) {
            logger.debug("Winning Bet!");
            result.setWonBet(true);
            result.setAmountShifted(wagerAmount * 2);

        } else if (leftValue == middleValue || rightValue == middleValue) {
            logger.debug("Lost the Bet! Hit on the edge");
            result.setWonBet(false);
            result.setPenaltyApplied(true);
            result.setAmountShifted(-1 * (wagerAmount * 2));
        } else {
            logger.debug("Lost the Bet!");
            result.setWonBet(false);
            result.setPenaltyApplied(false);
            result.setAmountShifted(-1 * wagerAmount);
        }


        return result;
    }
}
