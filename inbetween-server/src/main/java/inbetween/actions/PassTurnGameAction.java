package inbetween.actions;

import inbetween.models.actions.ActionRequest;
import inbetween.models.actions.PassTurnActionRequest;
import inbetween.models.enums.UserGameAction;
import inbetween.services.CardService;
import inbetween.services.GameService;
import inbetween.services.NextTurnMessagingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PassTurnGameAction implements GameAction {

    private final CardService cardService;
    private final GameService gameService;

    private final NextTurnMessagingService nextTurnMessagingService;

    public PassTurnGameAction(CardService cardService, GameService gameService, NextTurnMessagingService nextTurnMessagingService) {
        this.cardService = cardService;
        this.gameService = gameService;
        this.nextTurnMessagingService = nextTurnMessagingService;
    }

    @Override
    public boolean hasWork(UserGameAction gameAction) {
        return UserGameAction.PASS_TURN == gameAction;
    }

    @Override
    public ResponseEntity<?> perform(ActionRequest actionRequest) {

        if (actionRequest instanceof PassTurnActionRequest) {
            //Update the side cards
            cardService.revealTwoNewSideCards(actionRequest.getGameId());

            nextTurnMessagingService.updateNextTurnAndSendMessage(actionRequest.getGameId());
        }

        return ResponseEntity.ok().build();
    }

}