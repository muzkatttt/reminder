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

Получить все напоминания в списке:
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
