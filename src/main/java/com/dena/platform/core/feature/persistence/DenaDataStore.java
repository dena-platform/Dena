package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface DenaDataStore {

    List<DenaObject> storeObjects(String appName, String typeName, DenaObject... denaObjects);

    List<DenaObject> updateObjects(String appName, String typeName, DenaObject... denaObjects);

    long deleteObjects(String appName, String typeName, String... objectIds);

    long deleteRelation(String appName, String parentTypeName, String parentObjectId, String childTypeName, String childObjectId);

    long deleteRelation(String appName, String parentTypeName, String parentObjectId, String relationName);

    List<DenaObject> findObject(String appName, String typeName, String... objectId);

    List<DenaObject> findObjectRelation(String appName, String parentTypeName, String parentObjectId, String relationName, DenaPager denaPager);
}
