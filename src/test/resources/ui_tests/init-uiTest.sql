DROP TABLE users IF EXISTS;

CREATE TABLE users
(
    id BIGINT PRIMARY KEY NOT NULL IDENTITY,
    name VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(60) NOT NULL
);
CREATE UNIQUE INDEX table_name_email_uindex ON users (email);
CREATE UNIQUE INDEX table_name_name_uindex ON users (name);

INSERT INTO users (name, email, password) VALUES ('admin', 'admin@example.com', '$2a$06$YQgiGTYvarBdZRbNMhwDsuIzAmNQHR./qJnFUBdEW4MQLk.gdxYBu');
INSERT INTO users (name, email, password) VALUES ('user1', 'user1@example.com', '$2a$06$t485R2fTdVKH4pR357yg3eZJUBKipy8SXwrx6eGpc7GtE2goK1Bbq');