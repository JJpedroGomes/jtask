CREATE SEQUENCE lane_sequence START 1;

CREATE TABLE lane (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    users_id BIGINT,
    position INTEGER NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (users_id) REFERENCES users(id),
    CONSTRAINT pk_lane PRIMARY KEY (id)
);