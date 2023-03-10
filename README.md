# “Облачное хранилище”

## Описание проекта
Приложение представляет собой REST-сервис, разработанный в соответствии с [техническим заданием](./TechnicalTask.md).
Сервис предоставляет REST интерфейс для загрузки файлов и вывода списка уже загруженных файлов пользователя.
Все запросы к сервису авторизованы. Заранее подготовленное веб-приложение (FRONT) подключается к разработанному сервису
без доработок, а также использует функционал FRONT для авторизации, загрузки и вывода списка файлов пользователя.

## Реализация проекта:
- приложение разработано с использованием Spring Boot;
- использован сборщик пакетов Maven;
- в качестве системы хранения данных используется база данных MySql;
- для управления миграциями используется система Liquibase;
- для запуска используется Docker, Docker-compose;
- код размещен на GitHub;
- код покрыт unit тестами с использованием Mockito;
- добавлены интеграционные тесты с использованием Testcontainers;
- данные о пользователях хранятся в базе данных;
- первичная авторизация проходит по логину и паролю, последущие запросы при работе с файлами авторизуются на основе 
  полученного при первичной авторизации jwt-токена;
- файлы пользователей сохраняются в базе данных.

## Запуск приложения


- Создать jar архив
- Выполнить в терминале команду `docker-compose up`.
- Приложение доступно по адресу `http://localhost:8080`.
- При старте приложения в базу данных добавляются два тестовых пользователя с реквизитами доступа:
  1. login: `user1@mail.com`, password: `pass1`;
  2. login: `user2@mail.com`, password: `pass2`.

- Для выхода из приложения, находясь в терминале нажать комбинацию клавиш "Ctrl+C" или "Ctrl+Break".
- Для удаления Docker-контейнеров в терминале выполнить команду: `docker-compose down`.