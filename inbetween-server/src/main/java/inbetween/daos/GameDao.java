package inbetween.daos;

import inbetween.models.enums.GameStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GameDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public void updateGameStatus(int gameId, GameStatus gameStatus) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("gameId", gameId);
        parameters.addValue("gameStatus", gameStatus.name());
        namedParameterJdbcTemplate.update("UPDATE GAME set game_status = :gameStatus WHERE GAME_ID = :gameId", parameters);
    }

}