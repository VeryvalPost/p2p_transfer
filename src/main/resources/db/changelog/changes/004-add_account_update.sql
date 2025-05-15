--liquibase formatted sql

--changeset veryval:004-add_account_update
ALTER TABLE account
ADD COLUMN version BIGINT DEFAULT 0 NOT NULL,
ADD COLUMN initial_balance DECIMAL(19, 2) NOT NULL DEFAULT 0;

UPDATE account SET initial_balance = balance WHERE initial_balance = 0;

--rollback ALTER TABLE account DROP COLUMN version;
