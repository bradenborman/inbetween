package inbetween.actions;

import inbetween.models.actions.ActionRequest;
import inbetween.models.actions.SplitPassActionRequest;
import inbetween.models.enums.UserGameAction;
import inbetween.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PassSplitGameAction implements GameAction {

    /*
    Only the pass for the left side split pass - the right side pass is a normal end of turn pass
     */

    private final GameService gameService;

    public PassSplitGameAction(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public boolean hasWork(UserGameAction gameAction) {
        return gameAction == UserGameAction.PASS_SPLIT;
    }

    @Override
    public ResponseEntity<?> perform(ActionRequest actionRequest) {

        if (actionRequest instanceof SplitPassActionRequest) {
            SplitPassActionRequest splitActionRequest = (SplitPassActionRequest) actionRequest;

            //Passing on left Card Split - load the right split
            gameService.performSplitStartAndSendMessage(splitActionRequest.getUuid(), false);

        }

        return ResponseEntity.badRequest().build();
    }
}