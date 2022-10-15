package inbetween.controllers;

import inbetween.actions.GameAction;
import inbetween.models.actions.ActionRequest;
import inbetween.models.enums.UserGameAction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ActionController {

    private final List<GameAction> gameActions;

    public ActionController(List<GameAction> gameActions) {
        this.gameActions = gameActions;
    }

    @PostMapping("/perform:{action}")
    public ResponseEntity<Object> performAction(@PathVariable UserGameAction action, @RequestBody ActionRequest actionRequest) {
        Optional<GameAction> actionToPerform = gameActions.stream()
                .filter(x -> x.hasWork(action.name()))
                .findFirst();

        if (actionToPerform.isPresent()) {
            actionToPerform.get().perform(actionRequest);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}