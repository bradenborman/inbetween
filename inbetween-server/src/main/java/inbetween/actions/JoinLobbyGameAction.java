package inbetween.actions;

import inbetween.models.JoinedGameResponse;
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
            int playerId = gameService.joinLobbyWithPlayer(request.getGameId(), request.getDisplayName(), request.getPlayerRole(), false);
            String uuid = gameService.findJoinableGameByGameId(request.getGameId()).getUuid();
            JoinedGameResponse joinedGameResponse = new JoinedGameResponse(uuid, playerId, request.getDisplayName(), request.getPlayerRole());

            gameService.sendNewUserJoinedGameMessage(joinedGameResponse);

            return ResponseEntity.ok(joinedGameResponse);
        }

        return ResponseEntity.badRequest().build();
    }
}