--liquibase formatted sql

--changeset veryval:2-add-users

-- Пользователь 1
INSERT INTO user (name, date_of_birth, password)
VALUES ('Иван Иванов', '1990-01-15', 'pwd11111');

INSERT INTO account (user_id, balance)
VALUES (1, 1000.00);

INSERT INTO phone_data (phone, user_id)
VALUES ('79991234567', 1);

INSERT INTO email_data (email, user_id)
VALUES ('ivan.ivanov@mail.ru', 1);

-- Пользователь 2
INSERT INTO user (name, date_of_birth, password)
VALUES ('Мария Петрова', '1985-06-20', 'pwd22222');

INSERT INTO account (user_id, balance)
VALUES (2, 1500.00);

INSERT INTO phone_data (phone, user_id)
VALUES ('79876543210', 2);

INSERT INTO email_data (email, user_id)
VALUES ('maria.petrova@mail.ru', 2);

-- Пользователь 3
INSERT INTO user (name, date_of_birth, password)
VALUES ('Александр Сидоров', '1995-03-10', 'pwd33333');

INSERT INTO account (user_id, balance)
VALUES (3, 2000.00);

INSERT INTO phone_data (phone, user_id)
VALUES ('79111234567', 3);

INSERT INTO phone_data (phone, user_id)
VALUES ('79223456789', 3);

INSERT INTO email_data (email, user_id)
VALUES ('alex.sidorov@yandex.ru', 3);

-- Пользователь 4
INSERT INTO user (name, date_of_birth, password)
VALUES ('Елена Козлова', '1988-09-25', 'pwd44444');

INSERT INTO account (user_id, balance)
VALUES (4, 1200.00);

INSERT INTO phone_data (phone, user_id)
VALUES ('79334567890', 4);

INSERT INTO email_data (email, user_id)
VALUES ('elena.kozlova@mail.ru', 4);

INSERT INTO email_data (email, user_id)
VALUES ('elena.k2@yandex.ru', 4);

-- Пользователь 5
INSERT INTO user (name, date_of_birth, password)
VALUES ('Дмитрий Смирнов', '1992-11-30', 'pwd55555');

INSERT INTO account (user_id, balance)
VALUES (5, 3000.00);

INSERT INTO phone_data (phone, user_id)
VALUES ('79445678901', 5);

INSERT INTO phone_data (phone, user_id)
VALUES ('79556789012', 5);

INSERT INTO email_data (email, user_id)
VALUES ('dmitry.smirnov@mail.ru', 5);

INSERT INTO email_data (email, user_id)
VALUES ('d.smirnov2@yandex.ru', 5);