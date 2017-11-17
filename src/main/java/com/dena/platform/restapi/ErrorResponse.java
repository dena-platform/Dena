package com.dena.platform.restapi;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    @JsonProperty("status")
    private int status;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public static final class ErrorResponseBuilder {
        private int status;
        private String errorCode;
        private String message;

        private ErrorResponseBuilder() {
        }

        public static ErrorResponseBuilder anErrorResponse() {
            return new ErrorResponseBuilder();
        }

        public ErrorResponseBuilder withStatus(int status) {
            this.status = status;
            return this;
        }

        public ErrorResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder withErrorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ErrorResponse build() {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(status);
            errorResponse.setErrorCode(errorCode);
            errorResponse.setMessage(message);
            return errorResponse;
        }
    }
}
