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
                "SELECT * FROM PLAYERS WHERE game_joined = :gameId " +
                        "AND playing_role = :playingRole AND is_player_in_lobby_online = true " +
                        "ORDER BY player_Id ASC",
                parameters,
                (rs, rnum) -> {
                    Player player = new Player();
                    player.setPlayersTurn(rs.getBoolean("is_players_turn"));
                    player.setUserId(rs.getInt("player_Id"));
                    player.setDisplayName(rs.getString("display_name"));
                    player.setUserRole(UserRole.valueOf(rs.getString("playing_role")));
                    player.setScore(rs.getInt("score"));
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

    public void updateStatusOnline(String userIdJoined) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("playerId", userIdJoined);
        namedParameterJdbcTemplate.update("UPDATE PLAYERS SET is_player_in_lobby_online = true WHERE player_Id = :playerId", parameters);
    }

    public Player fillUserDetails(String playerId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("playerId", playerId);
        return namedParameterJdbcTemplate.queryForObject(
                "SELECT * FROM PLAYERS WHERE player_Id = :playerId",
                parameters,
                (rs, rnum) -> {
                    Player player = new Player();
                    player.setPlayersTurn(rs.getBoolean("is_players_turn"));
                    player.setUserId(rs.getInt("player_Id"));
                    player.setDisplayName(rs.getString("display_name"));
                    player.setUserRole(UserRole.valueOf(rs.getString("playing_role")));
                    player.setScore(rs.getInt("score"));
                    player.setGameJoined(rs.getInt("game_joined"));
                    return player;
                }
        );
    }

    public void updatePlayersSimpSessionId(String userId, String simpSessionId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("playerId", userId);
        parameters.addValue("sessionId", simpSessionId);
        namedParameterJdbcTemplate.update("UPDATE PLAYERS SET session_id = :sessionId WHERE player_Id = :playerId", parameters);
    }

    public boolean updateUserLeftBySessionId(String sessionIdClosed) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("sessionId", sessionIdClosed);
        return (namedParameterJdbcTemplate.update("UPDATE PLAYERS SET is_player_in_lobby_online = false WHERE session_id = :sessionId", parameters)) > 0;
    }

    public String findUserIDBySessionId(String sessionIdClosed) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("sessionId", sessionIdClosed);
        return namedParameterJdbcTemplate.queryForObject("SELECT player_Id FROM PLAYERS WHERE session_id = :sessionId LIMIT 1", parameters, String.class);
    }

    public Player findCurrentTurnPlayer(int gameId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("gameId", gameId);
        return namedParameterJdbcTemplate.queryForObject(
                "SELECT * FROM PLAYERS WHERE game_joined = :gameId AND is_players_turn = true LIMIT 1",
                parameters,
                (rs, rnum) -> {
                    Player player = new Player();
                    player.setPlayersTurn(rs.getBoolean("is_players_turn"));
                    player.setUserId(rs.getInt("player_Id"));
                    player.setDisplayName(rs.getString("display_name"));
                    player.setUserRole(UserRole.valueOf(rs.getString("playing_role")));
                    player.setScore(rs.getInt("score"));
                    player.setGameJoined(rs.getInt("game_joined"));
                    return player;
                }
        );
    }

    public Integer countPlayersActiveInLobby(int gameId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("gameId", gameId);
        return namedParameterJdbcTemplate.queryForObject("SELECT count(*) FROM PLAYERS " +
                "WHERE game_joined = :gameId AND is_player_in_lobby_online = true", parameters, Integer.class);
    }
}