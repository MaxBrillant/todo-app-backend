package com.ndashimye.firstapp.error;

public class TodoTaskNotFoundException extends Exception {

    public TodoTaskNotFoundException() {
        super("Todo task not found");
    }

    public TodoTaskNotFoundException(String message) {
        super(message);
    }

    public TodoTaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TodoTaskNotFoundException(Throwable cause) {
        super(cause);
    }

    protected TodoTaskNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
