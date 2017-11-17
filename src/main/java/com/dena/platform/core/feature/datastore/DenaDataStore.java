package com.dena.platform.core.feature.datastore;

import com.dena.platform.core.DenaObject;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface DenaDataStore {
    void storeObjects(List<DenaObject> denaObject, String appName, String typeName);

    DenaObject findObject(Integer objectId);

}
