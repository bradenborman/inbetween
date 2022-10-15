package inbetween.actions;

import inbetween.models.actions.ActionRequest;
import inbetween.models.enums.UserGameAction;
import inbetween.services.CardService;
import inbetween.services.GameService;
import org.springframework.http.ResponseEntity;

public class AcknowledgeResultAction implements GameAction {

    private final CardService cardService;
    private final GameService gameService;

    public AcknowledgeResultAction(CardService cardService, GameService gameService) {
        this.cardService = cardService;
        this.gameService = gameService;
    }

    @Override
    public boolean hasWork(UserGameAction actionString) {
        return UserGameAction.ACKNOWLEDGE_RESULTS == actionString;
    }

    @Override
    public ResponseEntity<?> perform(ActionRequest actionRequest) {

        cardService.revealTwoNewSideCards(actionRequest.getGameId());
        cardService.clearMiddleCard(actionRequest.getGameId());


        String idOfNextPlayer = gameService.advanceToNextPlayersTurn(actionRequest.getGameId());

        return ResponseEntity.ok().build();
    }
}