package com.muzkat.reminder.exception;

/**
 * Исключение, выбрасываемое при попытке зарегистрировать пользователя по email повторно.
 * Если пользователь пытается зарегистрироваться еще раз на уже зарегиcтрированный ранее email
 * может быть выброшено данное исключение
 */
public class UserAlreadyExistsException extends RuntimeException{

    /**
     * Метод создаёт новое исключение {@code UserAlreadyExistsException} с указанным сообщением
     * @param message описание причины исключения
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
