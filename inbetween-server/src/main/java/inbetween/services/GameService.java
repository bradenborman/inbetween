package inbetween.services;

import inbetween.daos.GameDao;
import org.springframework.stereotype.Service;

@Service
public class GameService {


    private final GameDao gameDao;

    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public void advanceToNextPlayersTurn(int gameId) {
        //TODO

    }

}