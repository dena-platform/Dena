package com.dena.platform.core.feature.datastore;

import com.dena.platform.common.exception.DenaException;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class DataStoreException extends DenaException {
    public DataStoreException(String message) {
        super(message);
    }

    public DataStoreException(Throwable ex) {
        super(ex);
    }
}
