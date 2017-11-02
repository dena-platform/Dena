package com.dena.platform.common.exception;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class DenaException extends RuntimeException {
    public DenaException(String message) {
        super(message);
    }

    public DenaException(Throwable ex) {
        super(ex);
    }
}
