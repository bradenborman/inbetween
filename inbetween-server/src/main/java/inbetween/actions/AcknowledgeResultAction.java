package inbetween.actions;

import inbetween.models.actions.ActionRequest;
import inbetween.models.enums.UserGameAction;
import inbetween.services.CardService;
import inbetween.services.GameService;

public class AcknowledgeResultAction implements GameAction {

    private final CardService cardService;
    private final GameService gameService;

    public AcknowledgeResultAction(CardService cardService, GameService gameService) {
        this.cardService = cardService;
        this.gameService = gameService;
    }

    @Override
    public boolean hasWork(String actionString) {
        return UserGameAction.ACKNOWLEDGE_RESULTS == UserGameAction.valueOf(actionString);
    }

    @Override
    public void perform(ActionRequest actionRequest) {

        cardService.revealTwoNewSideCards(actionRequest.getGameId());
        cardService.clearMiddleCard(actionRequest.getGameId());


        String idOfNextPlayer = gameService.advanceToNextPlayersTurn(actionRequest.getGameId());
    }
}