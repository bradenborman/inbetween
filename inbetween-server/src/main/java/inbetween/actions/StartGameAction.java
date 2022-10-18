package inbetween.actions;

import inbetween.models.actions.ActionRequest;
import inbetween.models.actions.StartGameActionRequest;
import inbetween.models.enums.GameStatus;
import inbetween.models.enums.UserGameAction;
import inbetween.services.CardService;
import inbetween.services.GameService;
import inbetween.services.NextTurnMessagingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class StartGameAction implements GameAction {

    private final CardService cardService;
    private final GameService gameService;
    private final NextTurnMessagingService nextTurnMessagingService;

    public StartGameAction(CardService cardService, GameService gameService, NextTurnMessagingService nextTurnMessagingService) {
        this.cardService = cardService;
        this.gameService = gameService;
        this.nextTurnMessagingService = nextTurnMessagingService;
    }

    @Override
    public boolean hasWork(UserGameAction gameAction) {
        return UserGameAction.START_GAME == gameAction;
    }

    @Override
    public ResponseEntity<?> perform(ActionRequest actionRequest) {
        if (actionRequest instanceof StartGameActionRequest) {
            StartGameActionRequest request = (StartGameActionRequest) actionRequest;
            int gameId = gameService.getGameIdByUUID(request.getUuidToStart());

            //Load in side cards for the first time
            cardService.revealTwoNewSideCards(gameId);

            gameService.setDefaultAnteForGameByPlayerCount(gameId);

            //Update game status to in session from open
            gameService.updateGameStatusAndSendMessage(gameId, GameStatus.IN_SESSION);

            nextTurnMessagingService.updateNextTurnAndSendMessage(gameId, false);

        }
        return ResponseEntity.ok().build();
    }

}