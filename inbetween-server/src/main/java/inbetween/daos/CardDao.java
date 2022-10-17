package inbetween.daos;

import inbetween.models.Deck;
import inbetween.models.PlayingCard;
import inbetween.models.enums.CardSuite;
import inbetween.models.enums.CardValue;
import inbetween.models.enums.PlayingCardColumnName;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CardDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public CardDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertDeck(int gameId, Deck deck) {
        deck.getShuffledDeck().forEach(playingCard -> {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("gameId", gameId);
            parameters.addValue("value", playingCard.getCardValue().name());
            parameters.addValue("suite", playingCard.getSuit().name());
            namedParameterJdbcTemplate.update("INSERT INTO QUEUED_CARDS (game_id, card_value, card_suite) VALUES (:gameId, :value, :suite)", parameters);
        });
    }

    public PlayingCard drawNextCard(int gameId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("gameId", gameId);
        PlayingCard playingCard = namedParameterJdbcTemplate.queryForObject("SELECT * FROM QUEUED_CARDS " +
                        "WHERE GAME_ID = :gameId AND has_been_drawn = false ORDER BY CARD_ID ASC LIMIT 1", parameters,
                (rs, rowNum) -> {
                    PlayingCard x = new PlayingCard(
                            CardValue.valueOf(rs.getString("card_value")),
                            CardSuite.valueOf(rs.getString("card_suite"))
                    );
                    x.setCardId(rs.getString("card_id"));
                    return x;
                }
        );

        MapSqlParameterSource declareCardDrawn = new MapSqlParameterSource();
        declareCardDrawn.addValue("gameId", gameId);
        declareCardDrawn.addValue("value", playingCard.getCardValue().name());
        declareCardDrawn.addValue("suite", playingCard.getSuit().name());

        namedParameterJdbcTemplate.update("UPDATE QUEUED_CARDS set has_been_drawn = true " +
                "WHERE game_id = :gameId AND card_value = :value AND card_suite = :suite", declareCardDrawn);

        return playingCard;
    }

    public void loadCardToBoard(int gameId, PlayingCardColumnName columnName, PlayingCard card) {
        MapSqlParameterSource loadCardParams = new MapSqlParameterSource();
        loadCardParams.addValue("gameId", gameId);
        loadCardParams.addValue("cardId", card.getCardId());
        namedParameterJdbcTemplate.update("UPDATE GAME_TABLE set " + columnName.getColumnName() + " = :cardId " +
                "WHERE game_id = :gameId", loadCardParams);
    }

    public void initGameTable(int gameId) {
        MapSqlParameterSource initTableParams = new MapSqlParameterSource();
        initTableParams.addValue("gameId", gameId);
        namedParameterJdbcTemplate.update("INSERT INTO GAME_TABLE (game_id) VALUES (:gameId)", initTableParams);
    }


    //Card can be null if its middle
    public PlayingCard selectCardShowingInGame(int gameId, PlayingCardColumnName columnName) {
        String sql = "SELECT CARD_VALUE, CARD_SUITE  FROM GAME_TABLE " +
                "JOIN QUEUED_CARDS ON (QUEUED_CARDS.CARD_ID  = GAME_TABLE." + columnName.getColumnName() + ") " +
                "WHERE QUEUED_CARDS .GAME_ID = :gameId";
        MapSqlParameterSource gameIdParams = new MapSqlParameterSource();
        gameIdParams.addValue("gameId", gameId);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, gameIdParams, (rs, rowNum) -> new PlayingCard(
                    CardValue.valueOf(rs.getString("card_value")),
                    CardSuite.valueOf(rs.getString("card_suite"))
            ));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void clearCard(int gameId, PlayingCardColumnName columnName) {
        String sql = "UPDATE GAME_TABLE set " + columnName.getColumnName() + " = null WHERE game_id = :gameId";
        MapSqlParameterSource gameIdParams = new MapSqlParameterSource();
        gameIdParams.addValue("gameId", gameId);
        namedParameterJdbcTemplate.update(sql, gameIdParams);
    }

    public Integer countUnitlNextShuffle(int gameId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("gameId", gameId);
        return namedParameterJdbcTemplate.queryForObject("SELECT count(*) FROM QUEUED_CARDS " +
                "WHERE game_id = :gameId AND has_been_drawn = false", params, Integer.class);
    }
}