# Card Management System

Система управления банковскими картами с аутентификацией и авторизацией.

## Требования

- Java 17+
- Docker и Docker Compose
- Maven

## Запуск приложения

1. Клонируйте репозиторий
2. Перейдите в директорию проекта
3. Запустите приложение с помощью Docker Compose:

```bash
docker-compose up -d
Приложение будет доступно по адресу: http://localhost:8080

API Документация
После запуска приложения документация API будет доступна по адресу:

Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI спецификация: http://localhost:8080/v3/api-docs

Тестирование
Для запуска тестов выполните:

bash
./mvnw test
Аутентификация
Зарегистрируйте пользователя:

POST /api/auth/signup

json
{
  "username": "user",
  "password": "password",
  "fullName": "User Name",
  "roles": ["user"]
}
Авторизуйтесь для получения JWT токена:

POST /api/auth/signin

json
{
  "username": "user",
  "password": "password"
}
Используйте полученный токен в заголовке Authorization: Bearer <token> для доступа к защищенным endpoint'ам.

Функциональность
Администратор
Управление пользователями

Создание, блокировка, активация, удаление карт

Просмотр всех карт

Пользователь
Просмотр своих карт (с пагинацией)

Запрос блокировки карты

Переводы между своими картами

Просмотр баланса