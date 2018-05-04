package com.dena.platform.restapi.exception;

import com.dena.platform.common.exception.DenaException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class DenaRestException extends RuntimeException {

    private Map<String, Object[]> messages = new HashMap<>();

    private int statusCode;

    private String errorCode;

    private Locale locale;

    public int getStatusCode() {
        return statusCode;
    }

    public Locale getLocale() {
        return locale;
    }

    public Map<String, Object[]> getMessages() {
        return messages;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public static DenaRestException buildException(DenaException denaException) {
        String errorCode = denaException.getErrorCode().getErrorCode();
        int httpStatusCode = denaException.getErrorCode().getHttpStatusCode();

        return DenaRestExceptionBuilder.aDenaRestException()
                .withStatusCode(httpStatusCode)
                .withErrorCode(errorCode)
                .addMessageCode(denaException.getErrorCode().getMessageCode(), null)
                .withCause(denaException)
                .build();

    }


    public static final class DenaRestExceptionBuilder {
        private int statusCode;
        private String errorCode;
        private Locale locale;
        private Throwable cause;

        private Map<String, Object[]> messages = new HashMap<>();

        private DenaRestExceptionBuilder() {
        }

        public static DenaRestExceptionBuilder aDenaRestException() {
            return new DenaRestExceptionBuilder();
        }

        public DenaRestExceptionBuilder withStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public DenaRestExceptionBuilder withLocale(Locale local) {
            this.locale = local;
            return this;
        }


        public DenaRestExceptionBuilder withErrorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }


        public DenaRestExceptionBuilder addMessageCode(String messageKey, Object[] params) {
            messages.put(messageKey, params);
            return this;
        }

        public DenaRestExceptionBuilder withCause(Throwable cause) {
            this.cause = cause;
            return this;
        }


        public DenaRestException build() {
            DenaRestException denaRestException = new DenaRestException();
            denaRestException.statusCode = this.statusCode;
            denaRestException.locale = this.locale;
            denaRestException.messages = this.messages;
            denaRestException.errorCode = this.errorCode;
            if (cause != null) {
                denaRestException.initCause(cause);
            }
            return denaRestException;
        }
    }
}
