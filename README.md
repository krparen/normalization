# Тестовое задание java-разработчика.

## Условие

https://github.com/bergstar/testcase

## Стандарт документации

Код по возможности самодокументируемый. Т.е. комментарии оставляются только по необходимости, в особо важных или
сложных местах.

## Ссылки на используемые зависимости

### Spring Boot Starter Web
https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web

### Project Lombok
https://mvnrepository.com/artifact/org.projectlombok/lombok

### Apache Commons
https://mvnrepository.com/artifact/org.apache.commons/commons-lang3

### Spring Boot Starter Test
https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test

### AssertJ Core
https://mvnrepository.com/artifact/org.assertj/assertj-core

## Инструкция по запуску

### Запуск приложения

Сделать чекаут репозитория на локальный компьютер.

Для запуска приложения в корне репозитория выполнить команду `./mvnw clean spring-boot:run`.

Альтернативы на случай проблем с maven wrapper:

- можно сделать то же самое через установленный maven `mvn clean spring-boot:run`
- можно собрать jar-файл через `mvn clean package`, и запустить полученный fat jar через
`java -jar ./target/normalization-0.0.1-SNAPSHOT.jar`
- можно запустить из Intellij Idea (или её аналог)

### Получение ОКВЭД-а по номеру телефона

Нужно выполнить POST-запрос на адрес http://localhost:8080/match с телом

```json
{
    "phone": "79998887766"
}
```

Телефон может быть в любом формате, на бэкенде все символы, кроме числовых, отбрасываются.

Пример ответа:

```json
{
    "normalizedPhone": "+79998887766",
    "okved": {
        "code": "66",
        "name": "Деятельность вспомогательная в сфере финансовых услуг и страхования"
    },
    "matchLength": 2
}
```

