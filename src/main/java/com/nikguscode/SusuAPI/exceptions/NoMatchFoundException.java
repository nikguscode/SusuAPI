package com.nikguscode.SusuAPI.exceptions;

public class NoMatchFoundException extends Exception {
    public NoMatchFoundException(String className) {
        super("No match found in class: " + className);
    }

    public NoMatchFoundException(String className, String method) {
        super("No match found in class: " + className + "\n"
                + "Method: " + method);
    }
}
