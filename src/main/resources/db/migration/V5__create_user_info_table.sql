CREATE TABLE user_info (
    id               BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name       VARCHAR(100),
    last_name        VARCHAR(100),
    birth_date       DATE,
    created_at       TIMESTAMP,
    last_modified_at TIMESTAMP
);