package inbetween.services;

import inbetween.daos.CardDao;
import inbetween.daos.GameDao;
import inbetween.daos.UserDao;
import inbetween.models.*;
import inbetween.models.actions.BetActionRequest;
import inbetween.models.enums.GameStatus;
import inbetween.models.enums.PlayingCardColumnName;
import inbetween.models.enums.UserRole;
import inbetween.utilities.DeckUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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

        int gameId = gameDao.initNewGame(lobbyName);
        cardDao.insertDeck(gameId, DeckUtility.initializeNewDeck());
        cardDao.initGameTable(gameId);
        int playerId = joinLobbyWithPlayer(gameId, displayName, userRole, true);
        JoinableGame joinableGame = gameDao.findJoinableGameByGameId(gameId);

        lobbyCreatedResponse.setGameId(gameId);
        lobbyCreatedResponse.setUserPlayingOnScreenId(playerId);
        lobbyCreatedResponse.setUuid(joinableGame.getUuid());

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

        GameStatusResponse gameStatusResponseUpdateMessage = new GameStatusResponse();
        gameStatusResponseUpdateMessage.setUuid(gameDao.getUUIDByGameId(gameId));
        gameStatusResponseUpdateMessage.setGameStatus(gameStatus);

        List<JoinableGame> allJoinableGamesUpdate = findAllJoinableGames();

        simpMessagingTemplate.convertAndSend("/topic/update-game-status", gameStatusResponseUpdateMessage);
        simpMessagingTemplate.convertAndSend("/topic/joinable-game-update", allJoinableGamesUpdate);

    }

    public void setDefaultAnteForGameByPlayerCount(int gameId) {
        int playersInLobby = gameDao.countParticipatingPlayersInGame(gameId);
        int potTotalToStart = playersInLobby * 200;
        gameDao.setDefaultAnteForGame(gameId, potTotalToStart);
    }

    @Transactional
    public void performScoreExchange(BetActionRequest betActionRequest, int amountShifted) {
        userDao.applyScoreChangeToUser(betActionRequest.getUserBettingId(), amountShifted);
        gameDao.applyScoreChangeToGamesPot(betActionRequest.getGameId(), (amountShifted) * -1);
    }

    public void performSplitStartAndSendMessage(String uuid, boolean isLeftPlayingCard) {
        logger.info("Starting split action..");
        int gameId = getGameIdByUUID(uuid);
        PlayingCard newEdgePlayingCard = cardDao.drawNextCard(gameId);

        cardDao.updateSplitCard(gameId, newEdgePlayingCard);

        SplitResponse splitResponse = new SplitResponse();
        splitResponse.setGameUUID(uuid);
        splitResponse.setNewEdgeCard(newEdgePlayingCard);
        splitResponse.setLeftPlayingCard(isLeftPlayingCard);



        simpMessagingTemplate.convertAndSend("/topic/split-card-incoming", splitResponse);
    }

    public List<JoinableGame> findAllJoinableGames() {
        return gameDao.findAllJoinableGames();
    }

    public JoinableGame findJoinableGameByGameId(int gameId) {
        return gameDao.findJoinableGameByGameId(gameId);
    }

    public void sendNewUserJoinedGameMessage(JoinedGameResponse joinedGameResponse) {
        simpMessagingTemplate.convertAndSend("/topic/update-player-list", joinedGameResponse);
    }

    public void sendBetPerformedUpdate(BetResult betResult) {
        simpMessagingTemplate.convertAndSend("/topic/bet-result", betResult);
    }

    public List<Player> playerListByUUID(String uuid) {
        int gameId = getGameIdByUUID(uuid);
        return userDao.selectPlayersFromGame(gameId);
    }

    public GameStatusResponse gameStatusByUUID(String uuid) {
        GameStatus gameStatus = gameDao.getGameStatusByUUID(uuid);
        GameStatusResponse response = new GameStatusResponse();
        response.setUuid(uuid);
        response.setGameStatus(gameStatus);
        return response;
    }

    public GameUpdateStartTurn getLatestStartOfTurnUpdateByUUID(String uuid) {
        int gameId = gameDao.getGameIdByUUID(uuid);
        GameUpdateStartTurn updateStartTurn = new GameUpdateStartTurn();
        updateStartTurn.setPlayerList(userDao.selectPlayersFromGame(gameId));
        updateStartTurn.setUuid(uuid);
        updateStartTurn.setLeftPlayingCard(cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.LEFT));
        updateStartTurn.setRightPlayingCard(cardDao.selectCardShowingInGame(gameId, PlayingCardColumnName.RIGHT));
        updateStartTurn.setCardsLeftUntilReshuffle(cardDao.countUnitlNextShuffle(gameId));
        updateStartTurn.setPotTotal(gameDao.countPotTotalInPlay(gameId));

        int maxBid = (userDao.findCurrentTurnPlayer(gameId).getScore() / 2);
        updateStartTurn.setMaxBidAllowed(maxBid);

        return updateStartTurn;
    }

    public int potTotalByGameId(int gameId) {
        return gameDao.countPotTotalInPlay(gameId);
    }

    public int getGameIdByUUID(String uuid) {
        return gameDao.getGameIdByUUID(uuid);
    }

    public String getUUIDByGameId(int gameId) {
        return gameDao.getUUIDByGameId(gameId);
    }

    public boolean validUserIdCommittingAction(int gameId, String userId) {
        String currentPlayersTurnId = gameDao.selectIdOfPlayersTurn(gameId);
        return Objects.equals(userId, currentPlayersTurnId);
    }
}