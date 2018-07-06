package com.dena.platform.common.exception;

/**
 * Each error code contain two part:
 * <li>message code that resolve to an message</li>
 * <li>error code that include two part: error_code - http_response_code  </li>
 *
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

public enum ErrorCode {

    // General Error - range 0-999
    RESOURCE_NOT_FOUND("dena.platform.restapi.exception.RESOURCE_NOT_FOUND", "0-404"),
    GENERAL("dena.platform.restapi.exception.GENERAL", "999-500"),

    // Input type & format exception - range 1000-1999
    INVALID_REQUEST("dena.platform.restapi.exception.INVALID_REQUEST", "1000-400"),
    INVALID_MEDIA_TYPE("dena.platform.restapi.exception.INVALID_MEDIA_TYPE", "1001-400"),

    // Data store exception - range 2000-2100
    GENERAL_DATA_STORE_EXCEPTION("dena.platform.restapi.exception.GENERAL_DATASTORE", "2000-500"),
    RELATION_INVALID_EXCEPTION("dena.platform.restapi.exception.RELATION_INVALID", "2001-400"),
    OBJECT_ID_INVALID_EXCEPTION("dena.platform.restapi.exception.ObjectId_INVALID", "2002-400"),
    OBJECT_ID_NOT_FOUND_EXCEPTION("dena.platform.restapi.exception.ObjectId_NOT_FOUND", "2003-400"),
    FIELD_NOT_FOUND_EXCEPTION("dena.platform.restapi.exception.FIELD_TYPE_NOT_FOUND", "2004-400"),

    // Schema management exception - range 2100-2200
    SCHEMA_ALREADY_EXIST_EXCEPTION("dena.platform.restapi.exception.SCHEMA_ALREADY_EXIST_EXCEPTION", "2100-400"),



    // User management exception - range 3000-3999
    USER_ALREADY_EXIST_EXCEPTION("dena.platform.restapi.exception.USER_ALREADY_EXIST", "3000-400"),
    EMAIL_FIELD_IS_INVALID("dena.platform.restapi.exception.EMAIL_FIELD_IS_INVALID", "3001-400"),
    PASSWORD_FIELD_IS_INVALID("dena.platform.restapi.exception.PASSWORD_FIELD_IS_INVALID", "3002-400"),
    NO_USER_WITH_THIS_EMAIL_FOUND("dena.platform.restapi.exception.NO_USER_WITH_THIS_EMAIL_FOUND", "3003-400"),
    BAD_CREDENTIAL("dena.platform.restapi.exception.BAD_CREDENTIAL", "3004-400"),

    // Application management exception - range 4000-4999
    APP_NAME_FIELD_IS_INVALID("dena.platform.restapi.exception.APP_FIELD_IS_INVALID", "4000-400"),
    CREATOR_FIELD_IS_INVALID("dena.platform.restapi.exception.CREATOR_FIELD_IS_INVALID", "4001-400"),
    APPLICATION_ALREADY_EXIST("dena.platform.restapi.exception.APPLICATION_ALREADY_EXIST", "4002-400"),

    // SEARCH exception - range 8000-8999
    CAN_NOT_PARSE_QUERY("dena.platform.restapi.exception.CAN_NOT_PARSE_QUERY", "8000-8000");


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
        return errorCode.split("-")[0];
    }

    public int getHttpStatusCode() {
        return Integer.valueOf(errorCode.split("-")[1]);
    }
}
