CREATE TABLE quotes
(
    id     bigint not null auto_increment primary key,
    text   varchar(255) not null,
    author varchar(100)
);