CREATE TABLE users (
    id       bigint not null auto_increment primary key,
    email    varchar(255) not null,
    password varchar(100) not null,
    role     varchar(100) not null
)