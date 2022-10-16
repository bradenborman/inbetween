package inbetween.daos;

import inbetween.models.Player;
import inbetween.models.enums.UserRole;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public UserDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void applyScoreChangeToUser(String userBettingId, int amountShifted) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userBettingId", userBettingId);
        parameters.addValue("amountShifted", amountShifted);
        namedParameterJdbcTemplate.update("UPDATE PLAYERS SET score = (score + :amountShifted) WHERE player_id = :userBettingId", parameters);
    }

    public List<Player> selectPlayersFromGame(int gameId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("gameId", gameId);
        parameters.addValue("playingRole", UserRole.PLAYER.name());
        return namedParameterJdbcTemplate.query(
                "SELECT * FROM PLAYERS WHERE game_joined = :gameId AND playing_role = :playingRole ORDER BY player_Id ASC",
                parameters,
                (rs, rnum) -> {
                    Player player = new Player();
                    player.setPlayersTurn(rs.getBoolean("is_players_turn"));
                    player.setUserId(rs.getInt("player_Id"));
                    player.setDisplayName(rs.getString("display_name"));
                    return player;
                }
        );
    }

    public void updateNextTurnForUser(int gameId, Player player) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("gameId", gameId);
        namedParameterJdbcTemplate.update("UPDATE PLAYERS SET is_players_turn = false WHERE game_joined = :gameId", parameters);


        MapSqlParameterSource parameters2 = new MapSqlParameterSource();
        parameters2.addValue("gameId", gameId);
        parameters2.addValue("playerId", player.getUserId());
        namedParameterJdbcTemplate.update("UPDATE PLAYERS SET is_players_turn = true WHERE game_joined = :gameId AND player_Id = :playerId", parameters2);
    }
}