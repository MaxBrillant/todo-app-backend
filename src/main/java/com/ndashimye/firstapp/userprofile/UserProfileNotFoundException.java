package com.ndashimye.firstapp.userprofile;

public class UserProfileNotFoundException extends Exception{

    public UserProfileNotFoundException() {
        super("User profile not found");
    }

    public UserProfileNotFoundException(String message) {
        super(message);
    }

    public UserProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserProfileNotFoundException(Throwable cause) {
        super(cause);
    }

    protected UserProfileNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
