package inbetween.actions;

import inbetween.models.BetResult;
import inbetween.models.actions.ActionRequest;
import inbetween.models.actions.BetActionRequest;
import inbetween.models.enums.UserGameAction;
import inbetween.services.CardService;
import inbetween.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
    public boolean hasWork(UserGameAction gameAction) {
        return UserGameAction.BET == gameAction;
    }

    @Override
    public ResponseEntity<?> perform(ActionRequest actionRequest) {
        if (actionRequest instanceof BetActionRequest) {

            //Validate that betAmount does not exceed half of user total - (paying the dummy whammy)
            //Validate bet came from the user

            BetActionRequest betActionRequest = (BetActionRequest) actionRequest;

            int gameId = gameService.getGameIdByUUID(betActionRequest.getUuidOfGame());
            betActionRequest.setGameId(gameId);

            BetResult betResult = cardService.performNewBet(betActionRequest);
            betResult.setUuidOfGame(betActionRequest.getUuidOfGame());
            int potTotal = gameService.potTotalByGameId(gameId);
            betResult.setPotTotal(potTotal);


            logger.info(betResult.toString());

            gameService.performScoreExchange(betActionRequest, betResult.getAmountShifted());

            //score change before grabbing player
            betResult.setPlayerList(gameService.playerListByUUID(betActionRequest.getUuidOfGame()));
            gameService.sendBetPerformedUpdate(betResult);


            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
}