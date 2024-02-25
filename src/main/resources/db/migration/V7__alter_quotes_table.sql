ALTER TABLE quotes
    DROP COLUMN author,
    ADD COLUMN created_at       TIMESTAMP,
    ADD COLUMN last_modified_at TIMESTAMP,
    ADD COLUMN created_by       VARCHAR(100);