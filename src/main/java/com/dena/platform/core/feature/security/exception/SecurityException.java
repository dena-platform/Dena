package com.dena.platform.core.feature.security.exception;

import com.dena.platform.common.exception.DenaException;
import com.dena.platform.common.exception.ErrorCode;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class SecurityException extends DenaException {
    private ErrorCode errorCode;

    public SecurityException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SecurityException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
