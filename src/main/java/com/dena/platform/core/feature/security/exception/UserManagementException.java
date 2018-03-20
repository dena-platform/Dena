package com.dena.platform.core.feature.security.exception;

import com.dena.platform.common.exception.DenaException;
import com.dena.platform.common.exception.ErrorCode;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class UserManagementException extends DenaException {
    public UserManagementException(String message) {
        super(message);
    }

    public UserManagementException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
