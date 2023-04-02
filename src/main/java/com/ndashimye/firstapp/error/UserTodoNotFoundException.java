package com.ndashimye.firstapp.error;

public class UserTodoNotFoundException extends Exception {

    public UserTodoNotFoundException() {
        super("User todo not found");
    }

    public UserTodoNotFoundException(String message) {
        super(message);
    }

    public UserTodoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserTodoNotFoundException(Throwable cause) {
        super(cause);
    }

    protected UserTodoNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
