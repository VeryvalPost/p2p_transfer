--liquibase formatted sql

--changeset veryval:003-transaction_data
CREATE TABLE transactions (
                       id BIGSERIAL PRIMARY KEY,
                       from_user_id BIGINT,
                       to_user_id BIGINT,
                       timestamp TIMESTAMP,
                       amount DECIMAL(19,2)
);
