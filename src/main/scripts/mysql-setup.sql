CREATE DATABASE IF NOT EXISTS quotes;

USE quotes;

CREATE TABLE quote (
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    author VARCHAR(100),
    text   VARCHAR(255) NOT NULL
);

INSERT INTO quote(author, text) values('me', 'random text 1');
INSERT INTO quote(author, text) values('a future me', 'random text 2');
INSERT INTO quote(author, text) values('', 'random text 3');
INSERT INTO quote(author, text) values(null, 'random text 4');