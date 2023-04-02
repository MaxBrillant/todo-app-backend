package com.ndashimye.firstapp.error;

public class UserSettingsNotFoundException extends Exception{

    public UserSettingsNotFoundException() {
        super("User settings not found");
    }

    public UserSettingsNotFoundException(String message) {
        super(message);
    }

    public UserSettingsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserSettingsNotFoundException(Throwable cause) {
        super(cause);
    }

    protected UserSettingsNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
