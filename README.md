

# Варианты запуска:

1. Запуск на удаленном сервере Linux
   a. Собрать проект с помощью Maven
   mvn clean install
   b. Скопировать на сервер, например, с помощью scp
   b. Запустить jar файл на сервере
   java -jar target/test-0.0.1-SNAPSHOT.jar
   c. Перейти в браузере по адресу http://Адрес удаленной машины:8080/swagger-ui/index.html
   Как вариант можно добавить в сервисы. И запустить через systemd

systemctl start test.service
systemctl enable test.service
systemctl status test.service

Должны быть установлены и сконфигурированы БД Postgres и Redis
______________________________
2. Запуск на локальной машине
   a. Собрать проект с помощью Maven
   mvn clean install
   b. Запустить jar файл на локальной машине
   java -jar target/test-0.0.1-SNAPSHOT.jar
   c. Перейти в браузере по адресу http://localhost:8080/swagger-ui/index.html
   Должны быть установлены и сконфигурированы БД Postgres и Redis
______________________________
3. Запуск в Docker
   a. Собрать проект с помощью Maven
   mvn clean install
   b. Собрать образ Docker
   docker build -t p2p-transfer .
   c. Запустить контейнер
   docker run -p 8080:8080 p2p-transfer
   d. Перейти в браузере по адресу http://localhost:8080/swagger-ui/index.html
   Вариант докерфайла:

FROM openjdk:17-jdk-alpine

ADD target/p2p-transfer-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]

Далее docker-compose.yml:
docker-compose up -d

______________________________

# Нестандартные решения:
1. Пришлось отказаться от таблицы USER, т.к. зарезервированное слово в Postgres.