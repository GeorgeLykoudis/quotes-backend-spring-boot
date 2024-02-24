ALTER TABLE quotes
    DROP COLUMN author;

ALTER TABLE quotes
    ADD COLUMN user_id          BIGINT,
    ADD COLUMN created_at       TIMESTAMP,
    ADD COLUMN last_modified_at TIMESTAMP;

ALTER TABLE quotes
    ADD CONSTRAINT
        FOREIGN KEY(user_id) REFERENCES users(id);