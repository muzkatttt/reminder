package com.muzkat.reminder.exception;


/**
 * Исключение, выбрасываемое при попытке использовать недопустимый email.
 * Если адрес не прошёл проверку на валидность с помощью внешнего сервиса
 * Hunter.io, может быть выброшено данное исключение
 */
public class InvalidEmailException extends RuntimeException {

    /**
     * Метод создаёт новое исключение {@code InvalidEmailException} с указанным сообщением
     * @param message описание причины исключения
     */
    public InvalidEmailException(String message) {
        super(message);
    }
}
