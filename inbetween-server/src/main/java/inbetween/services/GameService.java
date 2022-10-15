package inbetween.services;

import inbetween.daos.CardDao;
import inbetween.daos.GameDao;
import inbetween.utilities.DeckUtility;
import org.springframework.stereotype.Service;

@Service
public class GameService {


    private final GameDao gameDao;
    private final CardDao cardDao;

    public GameService(GameDao gameDao, CardDao cardDao) {
        this.gameDao = gameDao;
        this.cardDao = cardDao;
    }

    public String advanceToNextPlayersTurn(int gameId) {
        //TODO
        //Update order in DB
        //return the next one up

        return "BRENT";
    }

    public int createNewLobby() {
        int gameId = cardDao.initNewGame();
        cardDao.insertDeck(gameId, DeckUtility.initializeNewDeck());
        cardDao.initGameTable(gameId);
        return gameId;
    }
}