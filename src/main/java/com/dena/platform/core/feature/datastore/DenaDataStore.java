package com.dena.platform.core.feature.datastore;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.datastore.exception.DataStoreException;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface DenaDataStore {
    void storeObjects(List<DenaObject> denaObject, String appName, String typeName) throws DataStoreException;

    DenaObject findObject(Integer objectId);

}
