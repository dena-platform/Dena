package com.dena.platform.common.exception;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public enum ErrorCode {
    // Input type & format exception - range 00-99
    INVALID_REQUEST("com.dena.platform.restapi.exception.INVALID_REQUEST", "1"),
    INVALID_MEDIA_TYPE("com.dena.platform.restapi.exception.INVALID_MEDIA_TYPE", "2"),


    // Data store exception - range 100-199
    GENERAL_DATA_STORE_EXCEPTION("com.dena.platform.restapi.exception.GENERAL_DATASTORE_EXCEPTION", "1000"),
    RELATION_INVALID_EXCEPTION("com.dena.platform.restapi.exception.RELATION_INVALID_EXCEPTION", "1001"),
    ObjectId_INVALID_EXCEPTION("com.dena.platform.restapi.exception.ObjectId_INVALID_EXCEPTION", "1002"),


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
