--liquibase formatted sql

--changeset veryval:001-initial.data
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(500) NOT NULL,
                       date_of_birth DATE,
                       password VARCHAR(500) NOT NULL CHECK (LENGTH(password) >= 8 AND LENGTH(password) <= 500)
);

CREATE TABLE account (
                         id BIGSERIAL PRIMARY KEY,
                         users_id BIGINT NOT NULL UNIQUE,
                         balance DECIMAL(19, 2) NOT NULL CHECK (balance >= 0),
                         CONSTRAINT fk_account_user FOREIGN KEY (users_id) REFERENCES users(id)
);

CREATE TABLE email_data (
                            id BIGSERIAL PRIMARY KEY,
                            email VARCHAR(255) NOT NULL UNIQUE,
                            users_id BIGINT NOT NULL,
                            CONSTRAINT fk_user_email FOREIGN KEY (users_id) REFERENCES users(id)
);

CREATE TABLE phone_data (
                            id BIGSERIAL PRIMARY KEY,
                            phone VARCHAR(15) NOT NULL UNIQUE,
                            users_id BIGINT NOT NULL,
                            CONSTRAINT fk_user_phone FOREIGN KEY (users_id) REFERENCES users(id)
);