package com.develmagic.fileregexper.exception;

/**
 * Created by mejmo on 5.7.2017.
 */
public class FileRegexperException extends RuntimeException {
    public FileRegexperException() {
    }

    public FileRegexperException(String message) {
        super(message);
    }

    public FileRegexperException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileRegexperException(Throwable cause) {
        super(cause);
    }

    public FileRegexperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
