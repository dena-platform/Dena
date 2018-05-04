package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

public interface DenaDataStore {

    List<DenaObject> store(String appName, String tableName, DenaObject... denaObjects);

    List<DenaObject> update(String appName, String tableName, DenaObject... denaObjects);

    long delete(String appName, String tableName, String... objectIds);

    long deleteRelation(String appName, String parentTableName, String parentObjectId, String childTableName, String childObjectId);

    long deleteRelation(String appName, String parentTableName, String parentObjectId, String relationName);

    List<DenaObject> find(String appName, String tableName, String... objectId);

    /**
     * Find all object that exist in the given table.
     *
     * @param appName
     * @param tableName
     * @return
     */
    List<DenaObject> findAll(String appName, String tableName, DenaPager denaPager);

    List<DenaObject> findRelatedObject(String appName, String parentTableName, String parentObjectId, String relationName, DenaPager denaPager);
}
