package com.dena.platform.core.feature.user.exception;

import com.dena.platform.common.exception.DenaException;
import com.dena.platform.common.exception.ErrorCode;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class UserManagementException extends DenaException {


    public UserManagementException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public UserManagementException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
