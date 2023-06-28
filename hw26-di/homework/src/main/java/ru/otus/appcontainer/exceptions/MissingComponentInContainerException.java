package ru.otus.appcontainer.exceptions;

public class MissingComponentInContainerException extends RuntimeException {

    public MissingComponentInContainerException(String message) {
        super(message);
    }
}
