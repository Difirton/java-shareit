CREATE TABLE IF NOT EXISTS users
(
    id SERIAL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items_requests
(
    id SERIAL,
    description VARCHAR(1000) NOT NULL,
    user_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT request_pk PRIMARY KEY (id),
    CONSTRAINT user_item_request_fk FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS items
(
    id SERIAL,
    name VARCHAR(250) NOT NULL,
    description VARCHAR(600),
    available BOOLEAN NOT NULL,
    owner_id BIGINT NOT NULL,
    item_request_id BIGINT,
    CONSTRAINT item_pk PRIMARY KEY (id),
    CONSTRAINT owner_item_fk FOREIGN KEY (owner_id) REFERENCES users(id),
    CONSTRAINT item_request_item_fk FOREIGN KEY (item_request_id) REFERENCES items_requests(id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id SERIAL,
    start TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    finish TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status VARCHAR(20) NOT NULL,
    renter_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    CONSTRAINT booking_pk PRIMARY KEY (id),
    CONSTRAINT renter_booking_fk FOREIGN KEY (renter_id) REFERENCES users(id),
    CONSTRAINT item_booking_fk FOREIGN KEY (item_id) REFERENCES items(id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id SERIAL,
    comment_text VARCHAR(1000) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT comment_pk PRIMARY KEY (id),
    CONSTRAINT item_comment_fk FOREIGN KEY (item_id) REFERENCES items(id),
    CONSTRAINT author_comment_fk FOREIGN KEY (author_id) REFERENCES users(id)
);