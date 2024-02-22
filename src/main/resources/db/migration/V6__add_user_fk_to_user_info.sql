ALTER TABLE users
    ADD COLUMN user_info_id BIGINT;
    
ALTER TABLE users
    ADD CONSTRAINT fk_user_user_info
    FOREIGN KEY (user_info_id) REFERENCES user_info(id);