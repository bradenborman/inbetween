package inbetween.actions;

import inbetween.daos.GameDao;
import inbetween.models.actions.ActionRequest;
import inbetween.models.enums.GameStatus;
import inbetween.models.enums.UserGameActions;
import inbetween.services.CardService;
import org.springframework.stereotype.Service;

@Service
public class StartGameAction implements GameAction {

    private final CardService cardService;
    private final GameDao gameDao;


    public StartGameAction(CardService cardService, GameDao gameDao) {
        this.cardService = cardService;
        this.gameDao = gameDao;
    }

    @Override
    public boolean hasWork(String actionString) {
        return UserGameActions.START_GAME == UserGameActions.valueOf(actionString);
    }

    @Override
    public void perform(ActionRequest actionRequest) {
        //Load in side cards for the first time
        cardService.revealTwoNewSideCards(actionRequest.getGameId());

        //Update game status to in session from open
        gameDao.updateGameStatus(actionRequest.getGameId(), GameStatus.IN_SESSION);
    }

}