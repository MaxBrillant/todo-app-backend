package com.ndashimye.firstapp.todoproject;

public class TodoProjectNotFoundException extends Exception {

    public TodoProjectNotFoundException() {
        super("Todo project not found");
    }

    public TodoProjectNotFoundException(String message) {
        super(message);
    }

    public TodoProjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TodoProjectNotFoundException(Throwable cause) {
        super(cause);
    }

    protected TodoProjectNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
