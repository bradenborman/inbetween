package inbetween.daos;

import inbetween.models.enums.GameStatus;
import inbetween.models.enums.UserRole;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class GameDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public GameDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateGameStatus(int gameId, GameStatus gameStatus) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("gameId", gameId);
        parameters.addValue("gameStatus", gameStatus.name());
        namedParameterJdbcTemplate.update("UPDATE GAME set game_status = :gameStatus WHERE GAME_ID = :gameId", parameters);
    }

    public int joinLobbyWithPlayer(int gameId, String displayName, UserRole userRole) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("game_joined", gameId);
        parameters.put("display_name", displayName);
        parameters.put("playing_role", userRole.name());

        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("PLAYERS")
                .usingGeneratedKeyColumns("player_Id")
                .usingColumns("game_joined", "display_name", "playing_role")
                .executeAndReturnKey(parameters)
                .intValue();
    }
}