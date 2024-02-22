CREATE TABLE tokens (
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    token      VARCHAR(255) not null,
    token_type ENUM('BEARER'),
    revoked    BOOLEAN,
    expired    BOOLEAN,
    user_id    BIGINT,
    FOREIGN KEY(user_id) REFERENCES users(id)
);