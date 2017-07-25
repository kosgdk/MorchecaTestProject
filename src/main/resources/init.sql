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
INSERT INTO users (name, email, password) VALUES ('user2', 'user2@example.com', '$2a$06$WGmijVGFZeHY86NYAMqY8OOFKrB28szsrs6pbRz8UXBwa69M0Dm4.');
INSERT INTO users (name, email, password) VALUES ('user3', 'user3@example.com', '$2a$06$AotYiMzYvH6jc10IMc0IlOQaVsrFZpr7OLQupDlOE234iPTTAUP82');
INSERT INTO users (name, email, password) VALUES ('user4', 'user4@example.com', '$2a$06$pkAkncjS0FURA0Xep0bBWOABKqZSA9003t3.aoCmTLHR9g3UeoJ4C');
INSERT INTO users (name, email, password) VALUES ('user5', 'user5@example.com', '$2a$06$fdWxs4WcTwUf7emZ63GagONdx2xpNfiaTWlz4R3.noVV4fm9TptxC');