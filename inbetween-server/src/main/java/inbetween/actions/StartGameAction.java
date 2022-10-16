package inbetween.actions;

import inbetween.models.actions.ActionRequest;
import inbetween.models.actions.StartGameActionRequest;
import inbetween.models.enums.GameStatus;
import inbetween.models.enums.UserGameAction;
import inbetween.services.CardService;
import inbetween.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class StartGameAction implements GameAction {

    private final CardService cardService;
    private final GameService gameService;

    public StartGameAction(CardService cardService, GameService gameService) {
        this.cardService = cardService;
        this.gameService = gameService;
    }

    @Override
    public boolean hasWork(UserGameAction gameAction) {
        return UserGameAction.START_GAME == gameAction;
    }

    @Override
    public ResponseEntity<?> perform(ActionRequest actionRequest) {
        if (actionRequest instanceof StartGameActionRequest) {
            //Load in side cards for the first time
            cardService.revealTwoNewSideCards(actionRequest.getGameId());

            gameService.setDefaultAnteForGameByPlayerCount(actionRequest.getGameId());

            //Update game status to in session from open
            gameService.updateGameStatus(actionRequest.getGameId(), GameStatus.IN_SESSION);
        }
        return ResponseEntity.ok().build();
    }

}