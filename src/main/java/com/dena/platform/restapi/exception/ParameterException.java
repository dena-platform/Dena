package com.dena.platform.restapi.exception;

import com.dena.platform.common.exception.DenaException;
import com.dena.platform.common.exception.ErrorCode;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class ParameterException extends DenaException {
    public ParameterException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
