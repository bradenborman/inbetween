package inbetween.actions;

import inbetween.models.actions.ActionRequest;
import inbetween.models.actions.CreateLobbyActionRequest;
import inbetween.models.enums.UserGameAction;
import inbetween.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CreateLobbyGameAction implements GameAction {

    private static final Logger logger = LoggerFactory.getLogger(CreateLobbyGameAction.class);

    private final GameService gameService;

    public CreateLobbyGameAction(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public boolean hasWork(UserGameAction gameAction) {
        return UserGameAction.CREATE_LOBBY == gameAction;
    }

    @Override
    public ResponseEntity<?> perform(ActionRequest actionRequest) {
        if (actionRequest instanceof CreateLobbyActionRequest) {
            CreateLobbyActionRequest createLobbyActionRequest = (CreateLobbyActionRequest) actionRequest;

            int gameLobbyCreated = gameService.createNewLobby();

            logger.info("Player {} created lobby: {}", createLobbyActionRequest.getDisplayName(), gameLobbyCreated);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(gameLobbyCreated);
        }

        return ResponseEntity.badRequest().build();
    }

}