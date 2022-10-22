package ru.otus.jdbc.mapper.reflection.entity.exception;

/**
 * Исключение для сущности у которой отсутствует @Id.
 */
public class NotFoundIdFieldException extends RuntimeException {


    public NotFoundIdFieldException(String message) {
        super(message);
    }
}
