package com.dena.platform.common.exception;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public abstract class DenaException extends RuntimeException {

    protected ErrorCode errorCode;

    public DenaException(String message) {
        super(message);
    }

    public DenaException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract ErrorCode getErrorCode();
}
