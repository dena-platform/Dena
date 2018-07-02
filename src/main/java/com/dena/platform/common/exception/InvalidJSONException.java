package com.dena.platform.common.exception;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class InvalidJSONException extends DenaException {
    private ErrorCode errorCode;


    public InvalidJSONException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvalidJSONException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }


    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
