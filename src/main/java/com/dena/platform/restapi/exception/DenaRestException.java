package com.dena.platform.restapi.exception;

import com.dena.platform.common.exception.DenaException;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class DenaRestException extends DenaException {
    private String messageCode;
    private int statusCode;

    public String getMessageCode() {
        return messageCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static final class DenaRestExceptionBuilder {
        private String messageCode;
        private int statusCode;

        private DenaRestExceptionBuilder() {
        }

        public static DenaRestExceptionBuilder aDenaRestException() {
            return new DenaRestExceptionBuilder();
        }

        public DenaRestExceptionBuilder withMessageCode(String messageCode) {
            this.messageCode = messageCode;
            return this;
        }

        public DenaRestExceptionBuilder withStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public DenaRestException build() {
            DenaRestException denaRestException = new DenaRestException();
            denaRestException.statusCode = this.statusCode;
            denaRestException.messageCode = this.messageCode;
            return denaRestException;
        }
    }
}
