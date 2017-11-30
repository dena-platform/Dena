package com.dena.platform.core.feature.datastore;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.datastore.exception.DataStoreException;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface DenaDataStore {
    void storeObjects(List<DenaObject> denaObject, String appName, String typeName) throws DataStoreException;

    void updateObjects(List<DenaObject> denaObject, String appName, String typeName) throws DataStoreException;

    long deleteObjects(String appName, String typeName, List<String> objectIds) throws DataStoreException;

    DenaObject findObject(String objectId);

}
