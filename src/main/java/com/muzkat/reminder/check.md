Проверить метод создать напоминание 
POST http://localhost:8080/api/remind/create
тело запроса JSON:
{
"title": "wow! check and update",
"description": "wow! after update",
"dateOfRemind": "2025-03-10",
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
