package com.dena.platform.core.feature.datastore.exception;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class RelationInvalidException extends RuntimeException {
    public RelationInvalidException(String message) {
        super(message);
    }

    public RelationInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
