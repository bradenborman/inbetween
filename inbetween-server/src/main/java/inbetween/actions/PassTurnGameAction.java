package inbetween.actions;

import inbetween.models.actions.ActionRequest;
import inbetween.models.enums.UserGameAction;
import inbetween.services.CardService;
import inbetween.services.GameService;
import org.springframework.stereotype.Component;

@Component
public class PassTurnGameAction implements GameAction {

    private final CardService cardService;
    private final GameService gameService;

    public PassTurnGameAction(CardService cardService, GameService gameService) {
        this.cardService = cardService;
        this.gameService = gameService;
    }

    @Override
    public boolean hasWork(String actionString) {
        return UserGameAction.PASS_TURN == UserGameAction.valueOf(actionString);
    }

    @Override
    public void perform(ActionRequest actionRequest) {
        //Update the side cards
        cardService.revealTwoNewSideCards(actionRequest.getGameId());

        //TODO update to next persons turn
        gameService.advanceToNextPlayersTurn(actionRequest.getGameId());

        //TODO send results via websocket
    }

}