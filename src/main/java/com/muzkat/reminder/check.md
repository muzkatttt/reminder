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

Проверить отправку напоминания на электронную почту
(в целях проверки корректности работы подключилась к Mailtrap)
POST http://localhost:8080/api/email/send/73

Настройка бота в Telegram:
1. найти @BotFather -> отправить ему сообщение /start -> /newbot
2. Указать имя и юзернейм (должен оканчиваться на bot, я сделала kjavadeveloper_reminder_bot)
3. Получить токен — это будет API-ключ от бота 
4. Написать сообщение боту https://t.me/<юзернейм_бота>
5. В браузере отправить запрос к Telegram API по ссылке: https://api.telegram.org/bot<ТОКЕН_БОТА>/getUpdates
6. Отправить любое сообщение в свой чат-бот, обновить браузер, и в сообщении найти Id чата
7. Скопировать id чата и токен, они понадобятся, чтобы настроить отправку уведомлений в Телеграм.


Настройка JwtToken
Здесь можно сгенерировать онлайн хэш пароля через BCrypt-генератор онлайн:
https://bcrypt-generator.com

через терминал auth/login
curl -X POST http://localhost:8080/auth/login \
-H "Content-Type: application/json" \
-d '{"email":"kjavadeveloper@mail.ru", "password":"secret"}'

появится строка вида:
{"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJramF2YWRldmVsb3BlckBtYWlsLnJ1IiwiaWF0IjoxNzQ3MDg1NDM2LCJleHAiOjE3NDcwODkwMzZ9.9NPozVp0oOrgVTBUU7SP4B8pk4yPaGmo8DPXsm_fMyE"}% 
 в "" вставить смоделированный токен в запросе на
curl -v -X GET http://localhost:8080/api/remind/all \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJramF2YWRldmVsb3BlckBtYWlsLnJ1IiwiaWF0IjoxNzQ3NzY5Nzg4LCJleHAiOjE3NDc3NzMzODh9.Wo1RE441Kqbkfn7jJ1OhyyqSKB6tAhk47fduO8qz8xw"
в терминале выйдет список всех напоминаний (из контроллера RemindController: GET http://localhost:8080/api/remind/all)

создать напоминание через терминал:
curl -X POST http://localhost:8080/api/remind/create \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJramF2YWRldmVsb3BlckBtYWlsLnJ1IiwiaWF0IjoxNzQ3NzQzNTkyLCJleHAiOjE3NDc3NDcxOTJ9.SLY0xB9-qg2jc4Aj9GA1HJrGdVOG-abCF_YzO2bnTUw" \
-d '{
"title": "мой рекорд в беге на 5 км 19.05",
"description": "19.05.2025 я установила новый рекорд в беге на 5 км с результатом 27 минут 00 секунд",
"dateOfRemind": "2025-05-19",
"timeOfRemind": "18:50:00",
"userId": 1
}'

удалить напоминание через терминал:
curl -X DELETE http://localhost:8080/api/remind/105 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJramF2YWRldmVsb3BlckBtYWlsLnJ1IiwiaWF0IjoxNzQ3NzY2NTg4LCJleHAiOjE3NDc3NzAxODh9.DES3AgIYKaqq_dMxPe5K2yoDPR_zuCzhKGvwOaSDDPg"

Оставлю здесь напоминание: 
19.05.2025 я установила новый рекорд в беге на 5 км с результатом 27 минут 00 секунд.