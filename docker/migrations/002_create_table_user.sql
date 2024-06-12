CREATE SEQUENCE user_sequence START 1;

CREATE TABLE "user" (
    id BIGINT NOT NULL,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    birth_date DATE NOT NULL,
    CONSTRAINT PK_user PRIMARY KEY (id)
);