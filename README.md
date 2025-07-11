# Card Management System

Система управления банковскими картами с аутентификацией и авторизацией.

## Требования

- Java 17+
- Docker и Docker Compose
- Maven
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

### 1. Запуск через Docker (рекомендуется)

git clone https://github.com/Engel62/Bank_REST.git
cd Bank_REST
docker-compose up -d

2. Запустите сервисы:
   docker-compose up -d
   Приложение будет доступно: http://localhost:8080

## Ручная установка
Требования:

JDK 17+

PostgreSQL 13+

Maven 3.9+

bash
# Сборка
mvn clean package

# Настройка БД
psql -U postgres -c "CREATE DATABASE bank_rest;"

# Запуск
java -jar target/Bank_REST-0.0.1.jar
### Swagger UI: 
http://localhost:8080/swagger-ui.html

### OpenAPI спецификация: 
http://localhost:8080/v3/api-docs
### Логи
Просмотр логов приложения:
docker logs bank-card-app

Просмотр логов БД:
docker logs postgres-db