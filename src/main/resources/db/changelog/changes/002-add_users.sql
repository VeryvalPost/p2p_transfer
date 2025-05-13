--liquibase formatted sql

--changeset veryval:002-add-users

-- Пользователь 1
INSERT INTO users (name, date_of_birth, password)
VALUES ('Иван Иванов', '1990-01-15', 'pwd111111')
RETURNING id;

INSERT INTO account (user_id, balance)
VALUES (
           (SELECT id FROM users WHERE name = 'Иван Иванов' LIMIT 1),
           1000.00
       );

INSERT INTO phone_data (phone, user_id)
VALUES (
           '79991234567',
           (SELECT id FROM users WHERE name = 'Иван Иванов' LIMIT 1)
       );

INSERT INTO email_data (email, user_id)
VALUES (
           'ivan.ivanov@mail.ru',
           (SELECT id FROM users WHERE name = 'Иван Иванов' LIMIT 1)
       );

-- Пользователь 2
INSERT INTO users (name, date_of_birth, password)
VALUES ('Мария Петрова', '1985-06-20', 'pwd222222')
RETURNING id;

INSERT INTO account (user_id, balance)
VALUES (
           (SELECT id FROM users WHERE name = 'Мария Петрова' LIMIT 1),
           1500.00
       );

INSERT INTO phone_data (phone, user_id)
VALUES (
           '79876543210',
           (SELECT id FROM users WHERE name = 'Мария Петрова' LIMIT 1)
       );

INSERT INTO email_data (email, user_id)
VALUES (
           'maria.petrov@mail.ru',
           (SELECT id FROM users WHERE name = 'Мария Петрова' LIMIT 1)
       );

-- Пользователь 3
INSERT INTO users (name, date_of_birth, password)
VALUES ('Александр Сидоров', '1995-03-10', 'pwd333333')
RETURNING id;

INSERT INTO account (user_id, balance)
VALUES (
           (SELECT id FROM users WHERE name = 'Александр Сидоров' LIMIT 1),
           2000.00
       );

INSERT INTO phone_data (phone, user_id)
VALUES (
           '79111234567',
           (SELECT id FROM users WHERE name = 'Александр Сидоров' LIMIT 1)
       );

INSERT INTO phone_data (phone, user_id)
VALUES (
           '79223456789',
           (SELECT id FROM users WHERE name = 'Александр Сидоров' LIMIT 1)
       );

INSERT INTO email_data (email, user_id)
VALUES (
           'alex.sidorov@yandex.ru',
           (SELECT id FROM users WHERE name = 'Александр Сидоров' LIMIT 1)
       );

-- Пользователь 4
INSERT INTO users (name, date_of_birth, password)
VALUES ('Елена Козлова', '1988-09-25', 'pwd444444')
RETURNING id;

INSERT INTO account (user_id, balance)
VALUES (
           (SELECT id FROM users WHERE name = 'Елена Козлова' LIMIT 1),
           1200.00
       );

INSERT INTO phone_data (phone, user_id)
VALUES (
           '79334567890',
           (SELECT id FROM users WHERE name = 'Елена Козлова' LIMIT 1)
       );

INSERT INTO email_data (email, user_id)
VALUES (
           'elena.kozlova@mail.ru',
           (SELECT id FROM users WHERE name = 'Елена Козлова' LIMIT 1)
       );

INSERT INTO email_data (email, user_id)
VALUES (
           'elena.k2@yandex.ru',
           (SELECT id FROM users WHERE name = 'Елена Козлова' LIMIT 1)
       );

-- Пользователь 5
INSERT INTO users (name, date_of_birth, password)
VALUES ('Дмитрий Смирнов', '1992-11-30', 'pwd555555')
RETURNING id;

INSERT INTO account (user_id, balance)
VALUES (
           (SELECT id FROM users WHERE name = 'Дмитрий Смирнов' LIMIT 1),
           3000.00
       );

INSERT INTO phone_data (phone, user_id)
VALUES (
           '79445678901',
           (SELECT id FROM users WHERE name = 'Дмитрий Смирнов' LIMIT 1)
       );

INSERT INTO phone_data (phone, user_id)
VALUES (
           '79556789012',
           (SELECT id FROM users WHERE name = 'Дмитрий Смирнов' LIMIT 1)
       );

INSERT INTO email_data (email, user_id)
VALUES (
           'dmitry.smirnov@mail.ru',
           (SELECT id FROM users WHERE name = 'Дмитрий Смирнов' LIMIT 1)
       );

INSERT INTO email_data (email, user_id)
VALUES (
           'd.smirnov2@yandex.ru',
           (SELECT id FROM users WHERE name = 'Дмитрий Смирнов' LIMIT 1)
       );