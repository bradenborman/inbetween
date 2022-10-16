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
import java.util.Optional;

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

    public int joinLobbyWithPlayer(int gameId, String displayName, UserRole userRole, boolean isPlayersTurn) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("game_joined", gameId);
        parameters.put("display_name", displayName);
        parameters.put("playing_role", userRole.name());
        parameters.put("is_players_turn", isPlayersTurn);

        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("PLAYERS")
                .usingGeneratedKeyColumns("player_Id")
                .usingColumns("game_joined", "display_name", "playing_role", "is_players_turn")
                .executeAndReturnKey(parameters)
                .intValue();
    }

    public int countParticipatingPlayersInGame(int gameId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("gameId", gameId);
        parameters.addValue("playingStatus", UserRole.PLAYER.name());
        return Optional.ofNullable(
                namedParameterJdbcTemplate.queryForObject("SELECT count(*) FROM PLAYERS WHERE game_joined = :gameId AND playing_role = :playingStatus", parameters, Integer.class)
        ).orElse(0);
    }

    public void setDefaultAnteForGame(int gameId, int potTotalToStart) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("gameId", gameId);
        parameters.addValue("potValue", potTotalToStart);
        namedParameterJdbcTemplate.update("UPDATE GAME_TABLE SET pot_value = :potValue WHERE GAME_ID = :gameId", parameters);
    }


    public void applyScoreChangeToGamesPot(int gameId, int amountShifted) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("gameId", gameId);
        parameters.addValue("amountShifted", amountShifted);
        namedParameterJdbcTemplate.update("UPDATE GAME_TABLE SET pot_value = (pot_value + :amountShifted) WHERE GAME_ID = :gameId", parameters);
    }
}