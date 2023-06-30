CREATE DATABASE IF NOT EXISTS quotes;

USE quotes;

CREATE TABLE quote (
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    author VARCHAR(100),
    text   VARCHAR(255) NOT NULL
);

INSERT INTO quote(author, text) values('author 1', 'random text 1');
INSERT INTO quote(author, text) values('author 2', 'random text 2');
INSERT INTO quote(author, text) values('author 3', 'random text 3');
INSERT INTO quote(author, text) values('author 4', 'random text 4');