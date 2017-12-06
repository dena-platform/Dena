package com.dena.platform.common.exception;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public abstract class DenaException extends RuntimeException {

    protected ErrorCode errorCode;

    public DenaException(String message) {

    }

    public DenaException(String message, Throwable cause) {

    }

    public abstract ErrorCode getErrorCode();
}
