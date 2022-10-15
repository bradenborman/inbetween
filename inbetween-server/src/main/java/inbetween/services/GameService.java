package inbetween.services;

import inbetween.daos.CardDao;
import inbetween.daos.GameDao;
import inbetween.models.LobbyCreatedResponse;
import inbetween.models.enums.UserRole;
import inbetween.utilities.DeckUtility;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {


    private final GameDao gameDao;
    private final CardDao cardDao;

    public GameService(GameDao gameDao, CardDao cardDao) {
        this.gameDao = gameDao;
        this.cardDao = cardDao;
    }

    public String advanceToNextPlayersTurn(int gameId) {
        return "BRENT";
    }

    @Transactional
    public LobbyCreatedResponse createNewLobbyAndInsertPlayer(String displayName, UserRole userRole) {
        LobbyCreatedResponse lobbyCreatedResponse = new LobbyCreatedResponse();

        int gameId = cardDao.initNewGame();
        cardDao.insertDeck(gameId, DeckUtility.initializeNewDeck());
        cardDao.initGameTable(gameId);
        int playerId = joinLobbyWithPlayer(gameId, displayName, userRole);

        lobbyCreatedResponse.setGameId(gameId);
        lobbyCreatedResponse.setUserPlayingOnScreenId(playerId);


        return lobbyCreatedResponse;
    }

    public int joinLobbyWithPlayer(int gameLobbyCreated, String displayName, UserRole userRole) {
        return gameDao.joinLobbyWithPlayer(gameLobbyCreated, displayName, userRole);
    }

}