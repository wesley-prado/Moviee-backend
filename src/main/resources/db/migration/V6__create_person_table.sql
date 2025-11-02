CREATE TABLE IF NOT EXISTS cinema.persons
(
    id            UUID                     NOT NULL,
    artistic_name VARCHAR(255)             NOT NULL,
    real_name     VARCHAR(255)             NOT NULL,
    dob           TIMESTAMP WITH TIME ZONE NOT NULL,
    about         VARCHAR(255),
    CONSTRAINT pk_persons PRIMARY KEY (id)
);
