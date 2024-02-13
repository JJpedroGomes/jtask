CREATE SEQUENCE task_sequence START 1;

CREATE TABLE task (
    id BIGINT NOT NULL,
    title VARCHAR NOT NULL,
    description TEXT,
    creation_date DATE NOT NULL,
    due_date DATE,
    conclusion_date DATE,
    status VARCHAR NOT NULL,
    CONSTRAINT PK_task PRIMARY KEY (id)
);