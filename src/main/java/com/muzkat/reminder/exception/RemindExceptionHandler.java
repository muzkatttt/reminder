package com.muzkat.reminder.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Глобальный обработчик исключений для RemindController
 * <p>Класс перехватывает исключения, возникающие в контроллерах
 * и преобразует их в структурированный JSON-ответ с HTTP-статусом и описанием ошибки</p>
 * @author ekaterinarodionova
 */

@RestControllerAdvice
public class RemindExceptionHandler {

    /**
     * Метод обрабатывает исключение, возникающее, когда напоминание не найдено.
     * @param e исключение, связанное с отсутствием записи в базе данных
     * @return ответ с HTTP-статусом 404 Not Found и сообщением об ошибке
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(EntityNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }


    /**
     * Метод обрабатывает исключение, возникающее при нарушении целостности данных в базе.
     * @param e исключение, связанное с ошибками в базе данных
     * @return ответ с HTTP-статусом 400 Bad Request и сообщением об ошибке
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityException(DataIntegrityViolationException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST,
                "Ошибка базы данных: " + e.getRootCause().getMessage());
    }

    /**
     * Метод обрабатывает исключение, возникающее при ошибке валидации данных.
     * @param e исключение, связанное с нарушением ограничений валидации
     * @return ответ с HTTP-статусом 400 Bad Request и сообщением о нарушении валидации
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ConstraintViolationException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Ошибка валидации: " + e.getMessage());
    }

    /**
     * Метод обрабатывает все неожиданные ошибки, возникшие в процессе работы приложения.
     * @param e общее исключение, которое не попало под другие обработчики
     * @return ответ с HTTP-статусом 500 Internal Server Error и сообщением об ошибке
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера: " + e.getMessage());
    }

    /**
     * Метод обрабатывает исключения {@link NoSuchElementException},
     * возникшие при отсутствии запрашиваемого объекта.
     * @param e исключение NoSuchElementException, содержащее информацию о причине ошибки
     * @return ответ с HTTP-статусом 404 NOT_FOUND и сообщением об ошибке
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND,
                "Объект не найден : " + e.getMessage());
    }

    /**
     * Вспомогательный метод для построения структурированного ответа об ошибке.
     * @param status HTTP-статус, который нужно вернуть
     * @param message сообщение об ошибке
     * @return объект {@link ResponseEntity} с телом ошибки и статусом
     */
        private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
