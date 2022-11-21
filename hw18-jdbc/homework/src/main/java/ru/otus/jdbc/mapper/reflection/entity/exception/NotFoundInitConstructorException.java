package ru.otus.jdbc.mapper.reflection.entity.exception;

/**
 * Исключение при отсутствии конструктора со всеми полями.
 */
public class NotFoundInitConstructorException extends RuntimeException {


    public NotFoundInitConstructorException(String message) {
        super(message);
    }

    public NotFoundInitConstructorException(String message, Throwable cause) {
        super(message, cause);
    }
}
