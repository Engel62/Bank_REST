# Card Management System

Система управления банковскими картами с аутентификацией и авторизацией.

## Требования

- Java 17+
- Docker и Docker Compose
- Maven

## Запуск приложения

# Bank Card Management System

Система управления банковскими картами с аутентификацией JWT и возможностью денежных переводов.

## Технологии
- Java 17
- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- PostgreSQL 13
- Liquibase 
- Docker + Docker Compose
- OpenAPI 3.0 (Swagger UI)

## Требования
- JDK 17+
- Maven 3.9+
- Docker 20+ и Docker Compose
- PostgreSQL 13 (или использовать контейнер)

## Быстрый запуск через Docker

1. Клонируйте репозиторий:
git clone https://github.com/ваш-репозиторий.git
cd bank-card-system
2. Запустите сервисы:
   docker-compose up -d
   Приложение будет доступно: http://localhost:8080



## Сборка и запуск

mvn clean package
java -jar target/bank-card-system-0.0.1.jar




Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI спецификация: http://localhost:8080/v3/api-docs

Аутентификация
Зарегистрируйте пользователя:

bash
curl -X POST "http://localhost:8080/api/auth/signup" \
-H "Content-Type: application/json" \
-d '{
  "username": "user",
  "password": "password",
  "fullName": "Test User"
}'
Получите токен:

bash
curl -X POST "http://localhost:8080/api/auth/signin" \
-H "Content-Type: application/json" \
-d '{
  "username": "user",
  "password": "password"
}'
Используйте полученный токен в заголовке:

text
Authorization: Bearer <ваш_токен>
Администрирование
Для доступа к админ-панели создайте пользователя с ролью ADMIN:

bash
curl -X POST "http://localhost:8080/api/auth/signup" \
-H "Content-Type: application/json" \
-d '{
  "username": "admin",
  "password": "admin123",
  "fullName": "Admin",
  "roles": ["admin"]
}'
Остановка сервисов
bash
docker-compose down
Логи
Просмотр логов приложения:

docker logs bank-card-app
Просмотр логов БД:

bash
docker logs postgres-db