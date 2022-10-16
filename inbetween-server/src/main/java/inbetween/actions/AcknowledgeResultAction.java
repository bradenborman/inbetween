package inbetween.actions;

import inbetween.models.actions.ActionRequest;
import inbetween.models.enums.UserGameAction;
import inbetween.services.CardService;
import inbetween.services.GameService;
import inbetween.services.NextTurnMessagingService;
import org.springframework.http.ResponseEntity;

public class AcknowledgeResultAction implements GameAction {

    private final CardService cardService;
    private final GameService gameService;

    private final NextTurnMessagingService nextTurnMessagingService;

    public AcknowledgeResultAction(CardService cardService, GameService gameService, NextTurnMessagingService nextTurnMessagingService) {
        this.cardService = cardService;
        this.gameService = gameService;
        this.nextTurnMessagingService = nextTurnMessagingService;
    }

    @Override
    public boolean hasWork(UserGameAction actionString) {
        return UserGameAction.ACKNOWLEDGE_RESULTS == actionString;
    }

    @Override
    public ResponseEntity<?> perform(ActionRequest actionRequest) {

        cardService.revealTwoNewSideCards(actionRequest.getGameId());
        cardService.clearMiddleCard(actionRequest.getGameId());



        nextTurnMessagingService.updateNextTurnAndSendMessage(actionRequest.getGameId());


        return ResponseEntity.ok().build();
    }
}