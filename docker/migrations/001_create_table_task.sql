CREATE SEQUENCE task_sequence START 1;

CREATE TABLE task (
    id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    creation_date DATE NOT NULL,
    due_date DATE,
    conclusion_date DATE,
    status VARCHAR(255) NOT NULL,
    lane_id BIGINT NOT NULL,
    position INTEGER NOT NULL,
    CONSTRAINT PK_task PRIMARY KEY (id)
);
