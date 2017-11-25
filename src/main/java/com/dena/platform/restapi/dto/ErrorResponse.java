package com.dena.platform.restapi.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class ErrorResponse {
    @JsonProperty("status")
    private int status;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("messages")
    private List<String> messages;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
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
        private List<String> messages = new ArrayList<>();

        private ErrorResponseBuilder() {
        }

        public static ErrorResponseBuilder anErrorResponse() {
            return new ErrorResponseBuilder();
        }

        public ErrorResponseBuilder withStatus(int status) {
            this.status = status;
            return this;
        }

        public ErrorResponseBuilder withMessages(List<String> message) {
            this.messages = message;
            return this;
        }

        public ErrorResponseBuilder withMessages(String message) {
            this.messages.add(message);
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
            errorResponse.setMessages(messages);
            return errorResponse;
        }
    }
}
