package com.dena.platform.restapi.exception;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public enum ErrorCodes {
    NOT_FOUND("com.dena.platform.restapi.exception.RESOURCE_NOT_FOUND", "0"),
    INVALID_REQUEST("com.dena.platform.restapi.exception.INVALID_REQUEST", "1"),

    // Data Store Exception
    GENERAL_DATA_STORE_EXCEPTION("com.dena.platform.restapi.exception.GENERAL_DATASTORE_EXCEPTION", "1000"),
    RELATION_INVALID_EXCEPTION("com.dena.platform.restapi.exception.RELATION_INVALID_EXCEPTION", "1001"),


    GENERAL("com.dena.platform.restapi.exception.GENERAL", "0");

    private String messageCode;
    private String errorCode;


    ErrorCodes(String messageCode, String errorCode) {
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
