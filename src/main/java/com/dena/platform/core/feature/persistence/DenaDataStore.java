package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface DenaDataStore {

    List<DenaObject> store(String appName, String typeName, DenaObject... denaObjects);

    List<DenaObject> update(String appName, String typeName, DenaObject... denaObjects);

    long delete(String appName, String typeName, String... objectIds);

    long deleteRelation(String appName, String parentTypeName, String parentObjectId, String childTypeName, String childObjectId);

    long deleteRelation(String appName, String parentTypeName, String parentObjectId, String relationName);

    List<DenaObject> find(String appName, String typeName, String... objectId);

    /**
     * Find all object that exist in given type.
     *
     * @param appName
     * @param typeName
     * @return
     */
    List<DenaObject> findAll(String appName, String typeName, DenaPager denaPager);

    List<DenaObject> findRelatedObject(String appName, String parentTypeName, String parentObjectId, String relationName, DenaPager denaPager);
}
