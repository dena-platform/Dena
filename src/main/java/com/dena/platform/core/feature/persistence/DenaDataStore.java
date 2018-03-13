package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface DenaDataStore {

    List<DenaObject> storeObjects(List<DenaObject> denaObject, String appName, String typeName);

    List<DenaObject> updateObjects(List<DenaObject> denaObject, String appName, String typeName);

    long deleteObjects(String appName, String typeName, List<String> objectIds);

    long deleteRelation(String appName, String parentTypeName, String parentObjectId, String childTypeName, String childObjectId);

    long deleteRelation(String appName, String parentTypeName, String parentObjectId, String childTypeName);

    List<DenaObject> findObject(String appName, String typeName, String... objectId);

    List<DenaObject> findObjectRelation(String appName, String parentType, String objectId, String targetType, DenaPager denaPager);
}
