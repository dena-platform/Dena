package com.dena.platform.core.feature.datastore.exception;

import com.dena.platform.common.exception.ErrorCode;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public class DataStoreException extends RuntimeException {
    private ErrorCode errorCode;

    public DataStoreException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DataStoreException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
