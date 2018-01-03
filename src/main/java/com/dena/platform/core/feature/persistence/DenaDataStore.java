package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface DenaDataStore {
    void storeObjects(List<DenaObject> denaObject, String appName, String typeName);

    void updateObjects(List<DenaObject> denaObject, String appName, String typeName);

    long deleteObjects(String appName, String typeName, List<String> objectIds);

    long deleteRelation(String appName, String typeName1, String objectId1, String typeName2, String objectId2);

    long deleteRelation(String appName, String typeName1, String objectId1, String typeName2);

    DenaObject findObject(String appName, String typeName, String objectId);

    List<DenaObject> findObjectRelation(String appName, String parentType, String objectId, String targetType, DenaPager denaPager);
}
