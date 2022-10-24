package inbetween.actions;

import inbetween.models.actions.ActionRequest;
import inbetween.models.actions.SplitActionRequest;
import inbetween.models.enums.UserGameAction;
import inbetween.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SplitActionGameAction implements GameAction {

    private final GameService gameService;

    public SplitActionGameAction(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public boolean hasWork(UserGameAction gameAction) {
        return UserGameAction.SPLIT == gameAction;
    }

    @Override
    public ResponseEntity<?> perform(ActionRequest actionRequest) {

        if (actionRequest instanceof SplitActionRequest) {
            SplitActionRequest splitActionRequest = (SplitActionRequest) actionRequest;

            gameService.performSplitStartAndSendMessage(splitActionRequest.getUuid(), true);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

}
