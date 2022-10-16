package inbetween.actions;

import inbetween.models.actions.ActionRequest;
import inbetween.models.actions.JoinLobbyActionRequest;
import inbetween.models.enums.UserGameAction;
import inbetween.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class JoinLobbyGameAction implements GameAction {

    private final GameService gameService;

    public JoinLobbyGameAction(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public boolean hasWork(UserGameAction gameAction) {
        return UserGameAction.JOIN_LOBBY == gameAction;
    }

    @Override
    public ResponseEntity<?> perform(ActionRequest actionRequest) {
        if (actionRequest instanceof JoinLobbyActionRequest) {
            JoinLobbyActionRequest request = (JoinLobbyActionRequest) actionRequest;
            gameService.joinLobbyWithPlayer(request.getGameId(), request.getDisplayName(), request.getPlayerRole(), false);
        }

        return ResponseEntity.ok().build();
    }
}