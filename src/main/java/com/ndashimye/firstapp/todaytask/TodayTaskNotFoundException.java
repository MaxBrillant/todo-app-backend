package com.ndashimye.firstapp.todaytask;

public class TodayTaskNotFoundException extends Exception {

    public TodayTaskNotFoundException() {
        super("Today task not found");
    }

    public TodayTaskNotFoundException(String message) {
        super(message);
    }

    public TodayTaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TodayTaskNotFoundException(Throwable cause) {
        super(cause);
    }

    protected TodayTaskNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
