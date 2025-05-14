--liquibase formatted sql

--changeset veryval:004-add_account_update
ALTER TABLE account
ADD COLUMN version BIGINT DEFAULT 0 NOT NULL;

--rollback ALTER TABLE account DROP COLUMN version;
