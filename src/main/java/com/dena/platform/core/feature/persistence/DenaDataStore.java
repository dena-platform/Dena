package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

public interface DenaDataStore {

    List<DenaObject> store(String appId, String tableName, DenaObject... denaObjects);

    List<DenaObject> mergeUpdate(String appId, String tableName, DenaObject... denaObjects);

    List<DenaObject> replaceUpdate(String appId, String tableName, DenaObject... denaObjects);

    /**
     * Delete objects with provided object id in data store.
     *
     * @param appId     name of app
     * @param tableName table name
     * @param objectIds id of object that should be deleted
     * @return Number of deleted object
     */
    long delete(String appId, String tableName, String... objectIds);

    /**
     * Delete relation between parent and child object.
     *
     * @param appId
     * @param parentTableName
     * @param parentObjectId
     * @param childTableName
     * @param childObjectId
     * @return
     */
    long deleteRelation(String appId, String parentTableName, String parentObjectId, String childTableName, String childObjectId);

    /**
     * Delete relation between parent and child object.
     *
     * @param appId
     * @param parentTableName
     * @param parentObjectId
     * @param relationName
     * @return
     */
    long deleteRelation(String appId, String parentTableName, String parentObjectId, String relationName);

    /**
     * Find object in table with specified object ids.
     *
     * @param appId
     * @param tableName
     * @param objectId
     * @return
     */
    List<DenaObject> find(String appId, String tableName, String... objectId);

    /**
     * Find all object that exist in the given table.
     *
     * @param appId
     * @param tableName
     * @return
     */
    List<DenaObject> findAll(String appId, String tableName, DenaPager denaPager);

    /**
     * Find all related object that exist in relation.
     * @param appId
     * @param parentTableName
     * @param parentObjectId
     * @param relationName
     * @param denaPager
     * @return
     */
    List<DenaObject> findRelatedObject(String appId, String parentTableName, String parentObjectId, String relationName, DenaPager denaPager);


}
