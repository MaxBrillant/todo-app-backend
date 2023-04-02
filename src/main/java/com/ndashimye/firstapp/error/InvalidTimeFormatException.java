package com.ndashimye.firstapp.error;

public class InvalidTimeFormatException extends Exception{
    public InvalidTimeFormatException() {
        super("Invalid time format");
    }

    public InvalidTimeFormatException(String message) {
        super(message);
    }

    public InvalidTimeFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTimeFormatException(Throwable cause) {
        super(cause);
    }

    protected InvalidTimeFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
