package ru.otus.exception;

public class NotHaveMoneyException extends RuntimeException {

    public NotHaveMoneyException(String message) {
        super(message);
    }
}
