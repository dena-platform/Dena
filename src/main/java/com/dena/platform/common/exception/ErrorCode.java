package com.dena.platform.common.exception;

/**
 * Each error code contain two part:
 * 1:message code that resolve to an message
 * 2:
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public enum ErrorCode {
    // Input type & format exception - range 00-99
    INVALID_REQUEST("com.dena.platform.restapi.exception.INVALID_REQUEST", "00400"),
    INVALID_MEDIA_TYPE("com.dena.platform.restapi.exception.INVALID_MEDIA_TYPE", "01400"),


    // Data store exception - range 100-199
    GENERAL_DATA_STORE_EXCEPTION("com.dena.platform.restapi.exception.GENERAL_DATASTORE_EXCEPTION", "100500"),
    RELATION_INVALID_EXCEPTION("com.dena.platform.restapi.exception.RELATION_INVALID_EXCEPTION", "101400"),
    ObjectId_INVALID_EXCEPTION("com.dena.platform.restapi.exception.ObjectId_INVALID_EXCEPTION", "102400"),


    GENERAL("com.dena.platform.restapi.exception.GENERAL", "0");

    private String messageCode;
    private String errorCode;


    ErrorCode(String messageCode, String errorCode) {
        this.messageCode = messageCode;
        this.errorCode = errorCode;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
