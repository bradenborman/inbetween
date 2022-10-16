package inbetween.services;

import inbetween.daos.CardDao;
import inbetween.daos.GameDao;
import inbetween.daos.UserDao;
import inbetween.models.JoinableGame;
import inbetween.models.LobbyCreatedResponse;
import inbetween.models.actions.BetActionRequest;
import inbetween.models.enums.GameStatus;
import inbetween.models.enums.UserRole;
import inbetween.utilities.DeckUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameDao gameDao;
    private final CardDao cardDao;
    private final UserDao userDao;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public GameService(GameDao gameDao, CardDao cardDao, UserDao userDao, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameDao = gameDao;
        this.cardDao = cardDao;
        this.userDao = userDao;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Transactional
    public LobbyCreatedResponse createNewLobbyAndInsertPlayer(String displayName, UserRole userRole, String lobbyName) {
        LobbyCreatedResponse lobbyCreatedResponse = new LobbyCreatedResponse();

        int gameId = cardDao.initNewGame(lobbyName);
        cardDao.insertDeck(gameId, DeckUtility.initializeNewDeck());
        cardDao.initGameTable(gameId);
        int playerId = joinLobbyWithPlayer(gameId, displayName, userRole, true);

        lobbyCreatedResponse.setGameId(gameId);
        lobbyCreatedResponse.setUserPlayingOnScreenId(playerId);

        JoinableGame joinableGame = new JoinableGame();
        joinableGame.setLobbyName(lobbyName);
        joinableGame.setGameId(String.valueOf(gameId));

        simpMessagingTemplate.convertAndSend("/topic/new-lobby", joinableGame);

        return lobbyCreatedResponse;
    }

    public int joinLobbyWithPlayer(int gameLobby, String displayName, UserRole userRole, boolean isPlayersTurn) {
        logger.info("{} is joining {}", displayName, gameLobby);
        return gameDao.joinLobbyWithPlayer(gameLobby, displayName, userRole, isPlayersTurn);
    }

    public void updateGameStatusAndSendMessage(int gameId, GameStatus gameStatus) {
        logger.info("Updating game status to: {} in lobby {}", gameStatus.name(), gameId);
        gameDao.updateGameStatus(gameId, gameStatus);

        //TODO
    }

    public void setDefaultAnteForGameByPlayerCount(int gameId) {
        int playersInLobby = gameDao.countParticipatingPlayersInGame(gameId);
        int potTotalToStart = playersInLobby * 300;
        gameDao.setDefaultAnteForGame(gameId, potTotalToStart);
    }

    @Transactional
    public void performScoreExchange(BetActionRequest betActionRequest, int amountShifted) {
        userDao.applyScoreChangeToUser(betActionRequest.getUserBettingId(), amountShifted);
        gameDao.applyScoreChangeToGamesPot(betActionRequest.getGameId(), (amountShifted) * -1);
    }

}