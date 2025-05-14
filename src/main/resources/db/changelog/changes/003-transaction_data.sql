--liquibase formatted sql

--changeset veryval:003-transaction_data
CREATE TABLE transactions (
                       id BIGSERIAL PRIMARY KEY,
                       fromAccountId BIGINT,
                       toAccountId BIGINT,
                       timestamp DATE,
                       amount DECIMAL(19,2)
);
