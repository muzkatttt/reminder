### Тестирование работы приложения через Postman

Проверить метод создать напоминание
POST http://localhost:8080/api/remind/create
тело запроса JSON:
{
"title": "wow! check and update",
"description": "wow! after update",
"dateOfRemind": "2025-05-10",
"timeOfRemind": "18:00:00",
"userId": 1
}

Удалить напоминание по id:
@DeleteMapping("/{id}")
http://localhost:8080/api/remind/1

Проверить метод получения списка напоминаний по описанию
@GetMapping("/by-description/{description}")
Проверить метод: GET http://localhost:8080/api/remind/by-description/test_test_test

Проверить метод обновления напоминания по краткому описанию
@PutMapping("/by-title/{title}")
PUT http://localhost:8080/api/remind/by-title/five
тело запроса JSON:
{
"title": "birthday Evi",
"description": "update wow! today!!!",
"dateTimeOfRemind": "2025-03-11 11:00:00",
"userId": 1
}

Обновление напоминания по идентификатору
@PutMapping("/by-id/{id}")
PUT http://localhost:8080/api/remind/by-id/53
{
"title": "birthday Evi",
"description": "update wow! today!!!",
"dateTimeOfRemind": "2025-03-11 11:00:00",
"userId": 1
}

Получить все напоминания в списке в Postman:
@GetMapping("/all")
GET http://localhost:8080/api/remind/all

Проверить метод в Postman по краткому описанию, дате или времени:
@GetMapping("/filter")
GET http://localhost:8080/api/remind/filter?title=second
GET http://localhost:8080/api/remind/filter?date=2025-02-02
GET http://localhost:8080/api/remind/filter?time=20:00:00

Проверить метод сортировки напоминаний по краткому описанию дате или времени
Сортировка напоминаний
@GetMapping("/sorted")
Проверить метод в Postman
по краткому описанию: http://localhost:8080/api/remind/sorted?sortBy=title
по дате: http://localhost:8080/api/remind/sorted?sortBy=date
по времени: http://localhost:8080/api/remind/sorted?sortBy=time

### Проверить отправку напоминания на электронную почту

(в целях проверки корректности работы подключилась к Mailtrap)
POST http://localhost:8080/api/email/send/73

### Настройка бота в Telegram:

1. найти @BotFather -> отправить ему сообщение /start -> /newbot
2. Указать имя и юзернейм (должен оканчиваться на bot, я сделала kjavadeveloper_reminder_bot)
3. Получить токен — это будет API-ключ от бота
4. Написать сообщение боту https://t.me/<юзернейм_бота>
5. В браузере отправить запрос к Telegram API по ссылке: https://api.telegram.org/bot<ТОКЕН_БОТА>/getUpdates
6. Отправить любое сообщение в свой чат-бот, обновить браузер, и в сообщении найти Id чата
7. Скопировать id чата и токен, они понадобятся, чтобы настроить отправку уведомлений в Телеграм.

### Настройка JwtToken

Здесь можно сгенерировать онлайн хэш пароля через BCrypt-генератор онлайн:
https://bcrypt-generator.com

### Тестирование приложения через терминал

через терминал auth/login
curl -X POST http://localhost:8080/auth/login \
-H "Content-Type: application/json" \
-d '{"email":"kjavadeveloper@mail.ru", "password":"secret"}'

появится строка вида:
{"token":"
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJramF2YWRldmVsb3BlckBtYWlsLnJ1IiwiaWF0IjoxNzQ3MDg1NDM2LCJleHAiOjE3NDcwODkwMzZ9.9NPozVp0oOrgVTBUU7SP4B8pk4yPaGmo8DPXsm_fMyE"}%
в "" вставить смоделированный токен в запросе на
curl -v -X GET http://localhost:8080/api/remind/all \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJramF2YWRldmVsb3BlckBtYWlsLnJ1IiwiaWF0IjoxNzQ5NTAyMTM1LCJleHAiOjE3NDk1MDU3MzV9.mUjgLCX6nt9zAAYnDg2eYZAm-6xGHHg6n_zqhhxej-8"
в терминале выйдет список всех напоминаний (из контроллера RemindController: GET http://localhost:8080/api/remind/all)

создать напоминание через терминал:
curl -X POST http://localhost:8080/api/remind/create \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJramF2YWRldmVsb3BlckBtYWlsLnJ1IiwiaWF0IjoxNzQ5Njc2NTk2LCJleHAiOjE3NDk2ODAxOTZ9.3VBgWbo8OCEYTYWePSlUS5-n8o24ZgbG4zHUtxVE0Mw" \
-d '{
"title": "отправить пуш 19.06",
"description": "садись пиши проект",
"dateOfRemind": "2025-06-19",
"timeOfRemind": "08:00:00",
"userId": 1
}'


удалить напоминание через терминал:
curl -X DELETE http://localhost:8080/api/remind/105 \
-H "Authorization: Bearer
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJramF2YWRldmVsb3BlckBtYWlsLnJ1IiwiaWF0IjoxNzQ3NzY2NTg4LCJleHAiOjE3NDc3NzAxODh9.DES3AgIYKaqq_dMxPe5K2yoDPR_zuCzhKGvwOaSDDPg"

Оставлю здесь напоминание:
5.06.2025 улучшила результат в беге на 5 км до 26 минут 09 секунд.

### Подключить Swagger к проекту Reminder:

1. Подключить зависимость для swagger в build.gradle
2. Вносим изменения в SecurityConfig - разрешаем доступ без токена к "/v3/api-docs/**",
   "/swagger-ui.html", "/swagger-ui/**") без токена
3. Настраиваем URL в application.yml:
   _spring-doc:
   api-docs:
   path: /v3/api-docs
   swagger-ui:
   path: /swagger-ui.html_
4. Далее в браузере проверяем подключенные swagger и api-docs:

- http://localhost:8080//v3/api-docs
- http://localhost:8080/swagger-ui/index.html#/


### Тестирование работы RestTemplate & сервиса hunter.io по проверке email пользователя:
1. Сначала отправляем команду через терминал auth/login
   curl -X POST http://localhost:8080/auth/login \
   -H "Content-Type: application/json" \
   -d '{"email":"kjavadeveloper@mail.ru", "password":"secret"}'
2. Далее тестируем одной из двух команд (второй вариант предпочтительнее):
1)  
curl -X GET "http://localhost:8080/api/email/validate?email=kjavadeveloper@mail.ru" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJramF2YWRldmVsb3BlckBtYWlsLnJ1IiwiaWF0IjoxNzQ5NDEzMjM0LCJleHAiOjE3NDk0MTY4MzR9.Eps9zPiCMeyEm8Looc3wae40mlvQYR93MSQbH8NQffI"

2)
curl -G "http://localhost:8080/api/email/validate" \
--data-urlencode "email=kjavadeveloper@mail.ru" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJramF2YWRldmVsb3BlckBtYWlsLnJ1IiwiaWF0IjoxNzQ5NTAyMTM1LCJleHAiOjE3NDk1MDU3MzV9.mUjgLCX6nt9zAAYnDg2eYZAm-6xGHHg6n_zqhhxej-8"

### Проверить работу api.hunter (для настройки hHunterResponseDTO:
https://api.hunter.io/v2/email-verifier?email=kjavadeveloper@mail.ru&api_key= сюда вставить API KEY с сайта hunter.io

{
"data": {
"status": "accept_all",
"result": "risky",
"_deprecation_notice": "Using result is deprecated, use status instead",
"score": 73,
"email": "kjavadeveloper@mail.ru",
"regexp": true,
"gibberish": false,
"disposable": false,
"webmail": true,
"mx_records": true,
"smtp_server": true,
"smtp_check": true,
"accept_all": true,
"block": false,
"sources": []
},
"meta": {
"params": {
"email": "kjavadeveloper@mail.ru"
}
}
}

### Тесты
При написании тестов к методам классов следует придерживаться следующих шаблонов при названии методов:
1) по шаблону methodName_State_ExpectedResult (названиеТестируемогоМетода_Условие_ОжидаемыйРезультат)

2) Given/When/Then или Arrange/Act/Assert (Условие_Действие_ОжидаемыеРезультатыТеста)
