CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       date_of_birth DATE,
                       password VARCHAR(255) NOT NULL CHECK (LENGTH(password) >= 8 AND LENGTH(password) <= 50)
);

CREATE TABLE accounts (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL UNIQUE,
                          balance DECIMAL(19, 2) NOT NULL CHECK (balance >= 0),
                          initial_balance DECIMAL(19, 2) NOT NULL CHECK (initial_balance >= 0),
                          CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);