create table IF NOT EXISTS GAME (
  game_id bigint auto_increment,
  game_status varchar(16),
  game_initialized TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


create table IF NOT EXISTS QUEUED_CARDS (
  card_id bigint auto_increment,
  game_id bigint NOT NULL,
  card_value varchar(16),
  card_suite varchar(16),
  has_been_drawn TINYINT NOT NULL DEFAULT 0,
  FOREIGN KEY (game_id) REFERENCES GAME(game_id)
);

create table IF NOT EXISTS GAME_TABLE (
  table_id bigint auto_increment,
  game_id bigint NOT NULL,
  left_card varchar(16),
  right_card varchar(16),
  middle_card varchar(16),
  FOREIGN KEY (game_id) REFERENCES GAME(game_id)
);

create table IF NOT EXISTS PLAYERS (
  player_Id bigint auto_increment,
  display_name varchar(64) NOT NULL,
  game_joined bigint NOT NULL,
  playing_role varchar(16) NOT NULL,
  is_players_turn TINYINT NOT NULL DEFAULT 0,
  score INT NOT NULL DEFAULT 100,
  FOREIGN KEY (game_joined) REFERENCES GAME(game_id)
);