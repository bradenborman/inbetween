package inbetween.actions;

import inbetween.models.actions.ActionRequest;
import inbetween.models.enums.UserGameAction;
import org.springframework.http.ResponseEntity;

public interface GameAction {

    public boolean hasWork(UserGameAction actionString);

    public ResponseEntity<?> perform(ActionRequest actionRequest);

}
