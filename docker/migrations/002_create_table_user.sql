CREATE SEQUENCE user_sequence START 1;

CREATE TABLE users (
    id BIGINT NOT NULL,
    name VARCHAR NOT NULL,
    email VARCHAR UNIQUE NOT NULL,
    password VARCHAR NOT NULL,
    birth_date DATE NOT NULL,
    creation_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL,
    CONSTRAINT PK_user PRIMARY KEY (id)
);