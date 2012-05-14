BEGIN;

DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS rpg_systems CASCADE;
DROP TABLE IF EXISTS scenarios CASCADE;
DROP TABLE IF EXISTS char_sheets CASCADE;
DROP TABLE IF EXISTS sessions CASCADE;
DROP TABLE IF EXISTS participants CASCADE;

CREATE TABLE users (
  login             VARCHAR(20)    PRIMARY KEY,
  pass_md5          VARCHAR(30)    NOT NULL,
  state             CHAR           DEFAULT 'I',
  level             CHAR           DEFAULT '0',
  nickname          VARCHAR(30)    UNIQUE NOT NULL,
  location          VARCHAR(20),
  birthday          DATE,
  homepage          VARCHAR(40),
  show_chars        BOOLEAN        DEFAULT false,
  show_scenarios    BOOLEAN        DEFAULT false,
  comment_notify    BOOLEAN        DEFAULT false,
  session_notify    BOOLEAN        DEFAULT false,
  message_notify    BOOLEAN        DEFAULT false
);

CREATE TABLE messages (
  id              SERIAL          PRIMARY KEY,
  addressee       VARCHAR(20)     NOT NULL REFERENCES users(login),
  sender          VARCHAR(20)     NOT NULL REFERENCES users(login),
  time_stamp      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  topic           VARCHAR(20),
  content         TEXT            NOT NULL,
  was_read        BOOLEAN         DEFAULT false
);

CREATE TABLE comments (
  id              SERIAL         PRIMARY KEY,
  commentator     VARCHAR(20)    NOT NULL REFERENCES users(login),
  commentee       VARCHAR(20)    NOT NULL REFERENCES users(login),
  grade           SMALLINT       NOT NULL,
  comment         TEXT           NOT NULL,
  time_stamp      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rpg_systems (
  id                SERIAL          PRIMARY KEY,
  name              VARCHAR(20)     UNIQUE NOT NULL,
  description       TEXT,
  genre             VARCHAR(20),
  designer          VARCHAR(20),
  publisher         VARCHAR(20),
  year              DATE,
  char_sheet_dtd    TEXT
);

CREATE TABLE scenarios (
  id                SERIAL         PRIMARY KEY,
  owner             VARCHAR(20)    NOT NULL REFERENCES users(login),
  system            INTEGER        NOT NULL REFERENCES rpg_systems(id),
  type              CHAR           NOT NULL,
  players_count     SMALLINT       NOT NULL,
  content           TEXT           NOT NULL
);

CREATE TABLE char_sheets (
  id        SERIAL         PRIMARY KEY,
  owner     VARCHAR(20)    NOT NULL REFERENCES users(login),
  system    INTEGER        NOT NULL REFERENCES rpg_systems(id),
  xml_data  XML            NOT NULL
);

CREATE TABLE sessions (
  id            SERIAL        PRIMARY KEY,
  system        INTEGER       NOT NULL REFERENCES rpg_systems(id),
  owner         VARCHAR(20)   NOT NULL REFERENCES users(login),
  created       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
  time_stamp    TIMESTAMP     NOT NULL,
  type          CHAR          NOT NULL,
  location      VARCHAR(20)
);

CREATE TABLE participants (
  session     INTEGER        REFERENCES sessions(id),
  user        VARCHAR(20)    REFERENCES users(login),
  role        BOOLEAN        NOT NULL,
  state       BOOLEAN        DEFAULT false,
  PRIMARY KEY(session,login)
);

COMMIT;
