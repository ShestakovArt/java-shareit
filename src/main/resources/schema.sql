DROP TABLE IF EXISTS users, requests, items, comments, bookings;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                     name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email),
    CONSTRAINT pk_user PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS requests (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        description VARCHAR(1000) NOT NULL,
    requestor_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_request PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS items (
                                     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                     name VARCHAR(50) NOT NULL,
    descriprion VARCHAR(1000) NOT NULL,
    is_available BOOLEAN,
    owner_id BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    request_id BIGINT REFERENCES requests (id) ON DELETE CASCADE,
    CONSTRAINT pk_item PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        text VARCHAR(1000) NOT NULL,
    item_id BIGINT REFERENCES items (id) ON DELETE CASCADE NOT NULL,
    author_id BIGINT REFERENCES users (id) ON DELETE CASCADE  NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comments PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS bookings (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        item_id BIGINT REFERENCES items (id) ON DELETE CASCADE NOT NULL,
    booker_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    status VARCHAR(10),
    CONSTRAINT pk_booking PRIMARY KEY (id)
    );