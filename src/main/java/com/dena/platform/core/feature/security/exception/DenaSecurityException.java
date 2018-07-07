package com.dena.platform.core.feature.security.exception;

import com.dena.platform.common.exception.DenaException;
import com.dena.platform.common.exception.ErrorCode;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class DenaSecurityException extends DenaException {
    private ErrorCode errorCode;

    public DenaSecurityException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DenaSecurityException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
