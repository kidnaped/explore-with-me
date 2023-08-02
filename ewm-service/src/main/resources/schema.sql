DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilation_events CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name  VARCHAR(250) NOT NULL,
    email VARCHAR(254) UNIQUE NOT NULL,
    CONSTRAINT USERS_PK PRIMARY KEY (id),
    CONSTRAINT USERS_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT CATEGORIES_PK PRIMARY KEY (id),
    CONSTRAINT CATEGORIES_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY,
    title  VARCHAR(50)           NOT NULL,
    pinned BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT COMPILATIONS_PK PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    CONSTRAINT LOCATIONS_PK PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY,
    annotation         VARCHAR(2000)  NOT NULL,
    category_id        BIGINT        NOT NULL,
    confirmed_requests INTEGER       NOT NULL,
    created_on         TIMESTAMP     NOT NULL,
    description        VARCHAR(7000) NOT NULL,
    event_date         TIMESTAMP     NOT NULL,
    initiator_id       BIGINT        NOT NULL,
    location_id        BIGINT        NOT NULL,
    paid               BOOLEAN       NOT NULL,
    participant_limit  INTEGER       NOT NULL,
    published_on      TIMESTAMP,
    request_moderation BOOLEAN       NOT NULL,
    state              VARCHAR(16)   NOT NULL,
    title              VARCHAR(120)   NOT NULL,
    views              INTEGER DEFAULT 0,
    CONSTRAINT events_pk PRIMARY KEY (id),
    CONSTRAINT category_fk FOREIGN KEY (category_id)
        REFERENCES categories(id),
    CONSTRAINT initiator_fk FOREIGN KEY (initiator_id)
        REFERENCES users(id),
    CONSTRAINT location_fk FOREIGN KEY (location_id)
        REFERENCES locations(id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY,
    EVeNT_ID     BIGINT      NOT NULL,
    requester_id BIGINT      NOT NULL,
    status       VARCHAR(16) NOT NULL,
    created      TIMESTAMP   NOT NULL,
    CONSTRAINT requests_pk PRIMARY KEY (id),
    CONSTRAINT requests_event_fk FOREIGN KEY (event_id)
        REFERENCES events(id),
    CONSTRAINT requests_user_fk FOREIGN KEY (requester_id)
        REFERENCES users(id)
);

CREATE TABLE compilation_events
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY,
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,
    CONSTRAINT compilation_events_pk PRIMARY KEY (id),
    CONSTRAINT ce_compilation_fk FOREIGN KEY (compilation_id)
        REFERENCES compilations(id),
    CONSTRAINT ce_event_fk FOREIGN KEY (event_id)
        REFERENCES events(id)
);


