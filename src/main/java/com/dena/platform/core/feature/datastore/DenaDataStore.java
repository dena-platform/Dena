package com.dena.platform.core.feature.datastore;

import com.dena.platform.core.DenaObject;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface DenaDataStore {
    void storeObject(DenaObject denaObject);

    DenaObject findObject(Integer objectId);

}
