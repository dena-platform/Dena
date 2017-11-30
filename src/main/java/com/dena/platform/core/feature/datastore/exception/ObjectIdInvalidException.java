package com.dena.platform.core.feature.datastore.exception;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class ObjectIdInvalidException extends RuntimeException {
    public ObjectIdInvalidException(String message) {
        super(message);
    }

    public ObjectIdInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
