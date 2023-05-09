package com.ndashimye.firstapp.error;

public class AppEntityNotFoundException extends Exception{

    public AppEntityNotFoundException(Class entity) {
        super(entity.getName()+ " not found");
    }

    public AppEntityNotFoundException(String message) {
        super(message);
    }

    public AppEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppEntityNotFoundException(Throwable cause) {
        super(cause);
    }

    protected AppEntityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
