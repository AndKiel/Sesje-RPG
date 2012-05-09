BEGIN;

DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS user_data CASCADE;
DROP TABLE IF EXISTS user_settings CASCADE;
DROP TABLE IF EXISTS message CASCADE;
DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS rpg_system CASCADE;
DROP TABLE IF EXISTS scenario CASCADE;
DROP TABLE IF EXISTS char_sheet CASCADE;
DROP TABLE IF EXISTS session CASCADE;
DROP TABLE IF EXISTS participants CASCADE;

CREATE TABLE users (
  login     VARCHAR(20)   PRIMARY KEY,
  pass_md5  VARCHAR(30)   NOT NULL,
  state     VARCHAR(1)    DEFAULT 'i',
  level     VARCHAR(1)    DEFAULT '0'
);

CREATE TABLE user_data (
  login       VARCHAR(20)   REFERENCES users(login),
  nickname    VARCHAR(30)   UNIQUE NOT NULL,
  location    VARCHAR(20),
  birthday    DATE,
  homepage    VARCHAR(40),
  PRIMARY KEY (login)
);

CREATE TABLE user_settings (
  login             VARCHAR(20)    REFERENCES users(login),
  show_chars        BOOLEAN        DEFAULT false,
  show_scenarios    BOOLEAN        DEFAULT false,
  comment_notify    BOOLEAN        DEFAULT false,
  session_notify    BOOLEAN        DEFAULT false,
  message_notify    BOOLEAN        DEFAULT false,
  PRIMARY KEY (login)
);

CREATE TABLE message (
  id              SERIAL          PRIMARY KEY,
  addressee       VARCHAR(20)     NOT NULL REFERENCES users(login),
  sender          VARCHAR(20)     NOT NULL REFERENCES users(login),
  time_stamp      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  topic           VARCHAR(20),
  content         TEXT            NOT NULL,
  was_read        BOOLEAN         DEFAULT false
);

CREATE TABLE comment (
  id              SERIAL         PRIMARY KEY,
  commentator     VARCHAR(20)    NOT NULL REFERENCES users(login),
  commentee       VARCHAR(20)    NOT NULL REFERENCES users(login),
  grade           SMALLINT       NOT NULL,
  comment         TEXT           NOT NULL,
  time_stamp      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rpg_system (
  id                SERIAL          PRIMARY KEY,
  name              VARCHAR(20)     UNIQUE NOT NULL,
  description       TEXT,
  genre             VARCHAR(20),
  designer          VARCHAR(20),
  publisher         VARCHAR(20),
  year              DATE,
  char_sheet_dtd    TEXT
);

CREATE TABLE scenario (
  id                SERIAL         PRIMARY KEY,
  owner             VARCHAR(20)    NOT NULL REFERENCES users(login),
  system            INTEGER        NOT NULL REFERENCES rpg_system(id),
  type              VARCHAR(1)     NOT NULL,
  players_count     SMALLINT       NOT NULL,
  content           TEXT           NOT NULL
);

CREATE TABLE char_sheet (
  id        SERIAL         PRIMARY KEY,
  owner     VARCHAR(20)    NOT NULL REFERENCES users(login),
  system    INTEGER        NOT NULL REFERENCES rpg_system(id),
  xml_data  XML            NOT NULL
);

CREATE TABLE session (
  id            SERIAL        PRIMARY KEY,
  system        INTEGER       NOT NULL REFERENCES rpg_system(id),
  owner         VARCHAR(20)   NOT NULL REFERENCES users(login),
  time_stamp    TIMESTAMP     NOT NULL,
  type          VARCHAR(1)    NOT NULL,
  location      VARCHAR(20)
);

CREATE TABLE participants (
  session     INTEGER        REFERENCES session(id),
  login       VARCHAR(20)    REFERENCES users(login),
  role        BOOLEAN        NOT NULL,
  state       BOOLEAN        DEFAULT false,
  PRIMARY KEY(session,login)
);

COMMIT;