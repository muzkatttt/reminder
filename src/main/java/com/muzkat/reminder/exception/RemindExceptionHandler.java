package com.muzkat.reminder.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

/**
 * Глобальный обработчик исключений для RemindController
 * <p>Класс перехватывает исключения, возникающие в контроллере,
 * и преобразует их в HTTP-ответы с соответствующими статусами.</p>
 * @author ekaterinarodionova
 */

@ControllerAdvice
public class RemindExceptionHandler {

    /**
     * Метод обрабатывает исключение, возникающее, когда напоминание не найдено.
     * @param e исключение, связанное с отсутствием записи в базе данных
     * @return исключение с HTTP-статусом 404 Not Found
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseStatusException handleNotFoundException(EntityNotFoundException e) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }


    /**
     * Метод обрабатывает исключение, возникающее при нарушении целостности данных в базе.
     * @param e исключение, связанное с ошибками в базе данных
     * @return исключение с HTTP-статусом 400 Bad Request
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseStatusException handleDataIntegrityException(DataIntegrityViolationException e) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка базы данных: " + e.getMessage());
    }


    /**
     * Метод обрабатывает исключение, возникающее при ошибке валидации данных.
     * @param e исключение, связанное с нарушением ограничений валидации
     * @return исключение с HTTP-статусом 400 Bad Request
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseStatusException handleValidationException(ConstraintViolationException e) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка валидации: " + e.getMessage());
    }


    /**
     * Метод обрабатывает все неожиданные ошибки, возникшие в процессе работы приложения.
     * @param e общее исключение, которое не попало под другие обработчики
     * @return исключение с HTTP-статусом 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseStatusException handleGeneralException(Exception e) {
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка сервера: " + e.getMessage());
    }
}
