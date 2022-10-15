package inbetween.actions;

import inbetween.models.BetResult;
import inbetween.models.actions.ActionRequest;
import inbetween.models.actions.BetActionRequest;
import inbetween.models.enums.UserGameActions;
import inbetween.services.CardService;
import inbetween.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetGameAction implements GameAction {

    private static final Logger logger = LoggerFactory.getLogger(BetGameAction.class);

    private final CardService cardService;
    private final GameService gameService;

    public BetGameAction(CardService cardService, GameService gameService) {
        this.cardService = cardService;
        this.gameService = gameService;
    }

    @Override
    public boolean hasWork(String actionString) {
        return UserGameActions.BET == UserGameActions.valueOf(actionString);
    }

    @Override
    public void perform(ActionRequest actionRequest) {
        if (actionRequest instanceof BetActionRequest) {

            BetActionRequest betActionRequest = (BetActionRequest) actionRequest;

            BetResult betResult = cardService.performNewBet(betActionRequest);
            logger.info(betResult.toString());


            gameService.advanceToNextPlayersTurn(actionRequest.getGameId());

            //TODO return the middleCard to be displayed along with results of the bet
        }
    }
}