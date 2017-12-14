package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.exception.DataStoreException;

import java.util.List;
import java.util.Optional;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface DenaDataStore {
    void storeObjects(List<DenaObject> denaObject, String appName, String typeName) throws DataStoreException;

    void updateObjects(List<DenaObject> denaObject, String appName, String typeName) throws DataStoreException;

    long deleteObjects(String appName, String typeName, List<String> objectIds) throws DataStoreException;

    long deleteRelation(String appName, String typeName1, String objectId1, String typeName2, String objectId2);

    long deleteRelation(String appName, String typeName1, String objectId1, String typeName2);

    Optional<DenaObject> findObject(String appName, String typeName, String objectId) throws DataStoreException;

    DenaObject findObjectRelation(String appName, String parentType, String objectId, String targetType, DenaPager denaPager);
}
