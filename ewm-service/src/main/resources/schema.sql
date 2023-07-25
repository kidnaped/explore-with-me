DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS CATEGORIES CASCADE;
DROP TABLE IF EXISTS COMPILATIONS CASCADE;
DROP TABLE IF EXISTS LOCATIONS CASCADE;
DROP TABLE IF EXISTS EVENTS CASCADE;
DROP TABLE IF EXISTS REQUESTS CASCADE;
DROP TABLE IF EXISTS COMPILATION_EVENTS CASCADE;

CREATE TABLE IF NOT EXISTS USERS
(
    ID    BIGINT GENERATED BY DEFAULT AS IDENTITY,
    NAME  VARCHAR(32) NOT NULL,
    EMAIL VARCHAR(64) UNIQUE NOT NULL,
    CONSTRAINT USERS_PK PRIMARY KEY (ID),
    CONSTRAINT USERS_EMAIL UNIQUE (EMAIL)
);

CREATE TABLE IF NOT EXISTS CATEGORIES
(
    ID   BIGINT GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(32) NOT NULL,
    CONSTRAINT CATEGORIES_PK PRIMARY KEY (ID),
    CONSTRAINT CATEGORIES_NAME UNIQUE (NAME)
);

CREATE TABLE IF NOT EXISTS COMPILATIONS
(
    ID     BIGINT GENERATED BY DEFAULT AS IDENTITY,
    TITLE  VARCHAR(64)           NOT NULL,
    PINNED BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT COMPILATIONS_PK PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS LOCATIONS
(
    ID  BIGINT GENERATED BY DEFAULT AS IDENTITY,
    LAT DOUBLE PRECISION NOT NULL,
    LON DOUBLE PRECISION NOT NULL,
    CONSTRAINT LOCATIONS_PK PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS EVENTS
(
    ID                 BIGINT GENERATED BY DEFAULT AS IDENTITY,
    ANNOTATION         VARCHAR(512)  NOT NULL,
    CATEGORY_ID        BIGINT        NOT NULL,
    CONFIRMED_REQUESTS INTEGER       NOT NULL,
    CREATED_ON         TIMESTAMP     NOT NULL,
    DESCRIPTION        VARCHAR(3840) NOT NULL,
    EVENT_DATE         TIMESTAMP     NOT NULL,
    INITIATOR_ID       BIGINT        NOT NULL,
    LOCATION_ID        BIGINT        NOT NULL,
    PAID               BOOLEAN       NOT NULL,
    PARTICIPANT_LIMIT  INTEGER       NOT NULL,
    PUBLISHED_ON      TIMESTAMP,
    REQUEST_MODERATION BOOLEAN       NOT NULL,
    STATE              VARCHAR(16)   NOT NULL,
    TITLE              VARCHAR(64)   NOT NULL,
    VIEWS              INTEGER DEFAULT 0,
    CONSTRAINT EVENTS_PK PRIMARY KEY (ID),
    CONSTRAINT CATEGORY_FK FOREIGN KEY (CATEGORY_ID)
        REFERENCES CATEGORIES(ID),
    CONSTRAINT INITIATOR_FK FOREIGN KEY (INITIATOR_ID)
        REFERENCES USERS(ID),
    CONSTRAINT LOCATION_FK FOREIGN KEY (LOCATION_ID)
        REFERENCES LOCATIONS(ID)
);

CREATE TABLE IF NOT EXISTS REQUESTS
(
    ID           BIGINT GENERATED BY DEFAULT AS IDENTITY,
    EVENT_ID     BIGINT      NOT NULL,
    REQUESTER_ID BIGINT      NOT NULL,
    STATUS       VARCHAR(16) NOT NULL,
    CREATED      TIMESTAMP   NOT NULL,
    CONSTRAINT REQUESTS_PK PRIMARY KEY (ID),
    CONSTRAINT REQUESTS_EVENT_FK FOREIGN KEY (EVENT_ID)
        REFERENCES EVENTS(ID),
    CONSTRAINT REQUESTS_USER_FK FOREIGN KEY (REQUESTER_ID)
        REFERENCES USERS(ID)
);

CREATE TABLE COMPILATION_EVENTS
(
    ID             BIGINT GENERATED BY DEFAULT AS IDENTITY,
    COMPILATION_ID BIGINT NOT NULL,
    EVENT_ID       BIGINT NOT NULL,
    CONSTRAINT COMPILATION_EVENTS_PK PRIMARY KEY (ID),
    CONSTRAINT CE_COMPILATION_FK FOREIGN KEY (COMPILATION_ID)
        REFERENCES COMPILATIONS(ID),
    CONSTRAINT CE_EVENT_FK FOREIGN KEY (EVENT_ID)
        REFERENCES EVENTS(ID)
);


