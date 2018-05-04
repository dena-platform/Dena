package com.dena.platform.common.exception;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class DenaInternalException extends DenaException {
    public DenaInternalException(String message) {
        super(message);
    }

    public DenaInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public ErrorCode getErrorCode() {
        return null;
    }
}
