DROP TABLE IF EXISTS HITS CASCADE;

CREATE TABLE IF NOT EXISTS HITS
(
    ID   BIGINT GENERATED BY DEFAULT AS IDENTITY
        PRIMARY KEY,
    APP  VARCHAR(64) NOT NULL,
    URI  VARCHAR(64) NOT NULL,
    IP   VARCHAR(24) NOT NULL,
    TIME TIMESTAMP    NOT NULL
);