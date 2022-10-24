package inbetween.actions;

import inbetween.models.BetResult;
import inbetween.models.actions.ActionRequest;
import inbetween.models.actions.SplitBetActionRequest;
import inbetween.models.enums.PlayingCardColumnName;
import inbetween.models.enums.UserGameAction;
import inbetween.services.CardService;
import inbetween.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SplitBetGameAction implements GameAction {

    private final GameService gameService;
    private final CardService cardService;

    public SplitBetGameAction(GameService gameService, CardService cardService) {
        this.gameService = gameService;
        this.cardService = cardService;
    }

    @Override
    public boolean hasWork(UserGameAction gameAction) {
        return gameAction == UserGameAction.SPLIT_BET;
    }

    @Override
    public ResponseEntity<?> perform(ActionRequest actionRequest) {

        if (actionRequest instanceof SplitBetActionRequest) {
            SplitBetActionRequest splitBetActionRequest = (SplitBetActionRequest) actionRequest;


            int gameId = gameService.getGameIdByUUID(splitBetActionRequest.getUuid());

            boolean isValidUserMakingMove = gameService.validUserIdCommittingAction(gameId, splitBetActionRequest.getUserId());

            if (isValidUserMakingMove) {

                BetResult betResult = cardService.performNewSplitBet(
                        gameId,
                        splitBetActionRequest.getWagerAmount(),
                        PlayingCardColumnName.deriveCardToUse(splitBetActionRequest)
                );

                betResult.setUuidOfGame(splitBetActionRequest.getUuid());
                int potTotal = gameService.potTotalByGameId(gameId);
                betResult.setPotTotal(potTotal);

                gameService.performScoreExchange(gameId, splitBetActionRequest.getUserId(), betResult.getAmountShifted());

                //score change before grabbing player
                betResult.setPlayerList(gameService.playerListByUUID(splitBetActionRequest.getUuid()));
                gameService.sendBetPerformedUpdate(betResult);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.ok().build();
        }


        return ResponseEntity.badRequest().build();
    }

}