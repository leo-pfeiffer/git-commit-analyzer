PRAGMA foreign_keys = TRUE;

CREATE TABLE "user"
(
    user_id INTEGER AUTOINCREMENT,
    email   VARCHAR(35) NOT NULL,
    PRIMARY KEY (user_id),
    CHECK (email LIKE '_%@_%._%')
);

CREATE TABLE "gitlog"
(
    gitlog_id INTEGER AUTOINCREMENT,
    user_id   INTEGER NOT NULL,
    PRIMARY KEY (gitlog_id),
    FOREIGN KEY (user_id) REFERENCES user ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE "commit"
(
    commit_id      INTEGER AUTOINCREMENT,
    gitlog_id      INTEGER       NOT NULL,
    hash           VARCHAR(40)   NOT NULL UNIQUE,
    author_name    VARCHAR(30),
    author_mail    VARCHAR(35),
    header         VARCHAR(1000) NOT NULL,
    body           VARCHAR(1000),
    commit_type_id INTEGER,
    PRIMARY KEY (commit_id),
    FOREIGN KEY (gitlog_id) REFERENCES gitlog ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (commit_type_id) REFERENCES commit_type ON UPDATE CASCADE ON DELETE CASCADE,
    CHECK (author_mail LIKE '_%@_%._%')
);

CREATE TABLE "commit_type"
(
    commit_type_id INTEGER AUTOINCREMENT,
    name           VARCHAR(10),
    PRIMARY KEY (commit_type_id)
);

CREATE TABLE "node"
(
    node_id   INTEGER AUTOINCREMENT,
    commit_id INTEGER NOT NULL,
    additions INTEGER NOT NULL,
    deletions INTEGER NOT NULL,
    path      VARCHAR(100),
    PRIMARY KEY (node_id),
    FOREIGN KEY (commit_id) REFERENCES "commit" ON UPDATE CASCADE ON DELETE CASCADE
)