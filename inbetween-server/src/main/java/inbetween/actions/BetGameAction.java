package inbetween.actions;

import inbetween.models.BetResult;
import inbetween.models.actions.ActionRequest;
import inbetween.models.actions.BetActionRequest;
import inbetween.models.enums.UserGameAction;
import inbetween.services.CardService;
import inbetween.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
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
        return UserGameAction.BET == UserGameAction.valueOf(actionString);
    }

    @Override
    public void perform(ActionRequest actionRequest) {
        if (actionRequest instanceof BetActionRequest) {

            BetActionRequest betActionRequest = (BetActionRequest) actionRequest;

            BetResult betResult = cardService.performNewBet(betActionRequest);
            logger.info(betResult.toString());


            //TODO return the middleCard to be displayed along with results of the bet
        }
    }
}