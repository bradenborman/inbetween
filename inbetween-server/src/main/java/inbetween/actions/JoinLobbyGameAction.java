package inbetween.actions;

import inbetween.daos.UserDao;
import inbetween.models.JoinedGameResponse;
import inbetween.models.Player;
import inbetween.models.actions.ActionRequest;
import inbetween.models.actions.JoinLobbyActionRequest;
import inbetween.models.enums.UserGameAction;
import inbetween.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JoinLobbyGameAction implements GameAction {

    private final GameService gameService;
    private final UserDao userDao;

    public JoinLobbyGameAction(GameService gameService, UserDao userDao) {
        this.gameService = gameService;
        this.userDao = userDao;
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

            List<Player> playerList = userDao.selectPlayersFromGame(request.getGameId());

            JoinedGameResponse joinedGameResponse = new JoinedGameResponse(uuid, playerId, playerList);

            gameService.sendNewUserJoinedGameMessage(joinedGameResponse);

            return ResponseEntity.ok(joinedGameResponse);
        }

        return ResponseEntity.badRequest().build();
    }
}