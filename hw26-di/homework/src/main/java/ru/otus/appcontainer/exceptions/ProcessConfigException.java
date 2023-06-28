package ru.otus.appcontainer.exceptions;


public class ProcessConfigException extends RuntimeException {

    public ProcessConfigException(String message) {
        super(message);
    }

    public ProcessConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
