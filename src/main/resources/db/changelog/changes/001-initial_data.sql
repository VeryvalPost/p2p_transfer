--liquibase formatted sql

--changeset veryval:1-init
CREATE TABLE user (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(500) NOT NULL,
                      date_of_birth DATE,
                      password VARCHAR(500) NOT NULL CHECK (LENGTH(password) >= 8 AND LENGTH(password) <= 500)
);

CREATE TABLE account (
                         id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT NOT NULL UNIQUE,
                         balance DECIMAL(19, 2) NOT NULL CHECK (balance >= 0),
                         CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE email_data (
                            id BIGSERIAL PRIMARY KEY,
                            email VARCHAR(255) NOT NULL UNIQUE,
                            user_id BIGINT NOT NULL,
                            CONSTRAINT fk_user_email FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE phone_data (
                            id BIGSERIAL PRIMARY KEY,
                            phone VARCHAR(15) NOT NULL UNIQUE,
                            user_id BIGINT NOT NULL,
                            CONSTRAINT fk_user_phone FOREIGN KEY (user_id) REFERENCES user(id)
);