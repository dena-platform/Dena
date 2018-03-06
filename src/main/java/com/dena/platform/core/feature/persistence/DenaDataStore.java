package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface DenaDataStore {
    String UPDATE_TIME_FIELD = "update_time";

    String CREATE_TIME_FIELD = "create_time";


    List<DenaObject> storeObjects(List<DenaObject> denaObject, String appName, String typeName);

    List<DenaObject> updateObjects(List<DenaObject> denaObject, String appName, String typeName);

    long deleteObjects(String appName, String typeName, List<String> objectIds);

    long deleteRelation(String appName, String parentTypeName, String parentObjectId, String childTypeName, String childObjectId);

    long deleteRelation(String appName, String parentTypeName, String parentObjectId, String childTypeName);

    DenaObject findObject(String appName, String typeName, String objectId);

    List<DenaObject> findObjectRelation(String appName, String parentType, String objectId, String targetType, DenaPager denaPager);
}
