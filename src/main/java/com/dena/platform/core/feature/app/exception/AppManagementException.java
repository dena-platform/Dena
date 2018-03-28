package com.dena.platform.core.feature.app.exception;

import com.dena.platform.common.exception.DenaException;
import com.dena.platform.common.exception.ErrorCode;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class AppManagementException extends DenaException {


    public AppManagementException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AppManagementException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
