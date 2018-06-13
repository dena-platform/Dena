package com.dena.platform.core.feature.search;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nazarpour.
 */
public class MockDataStore implements DenaDataStore {

    @Override
    public List<DenaObject> store(String appName, String tableName, DenaObject... denaObjects) {
        return null;
    }

    @Override
    public List<DenaObject> mergeUpdate(String appName, String tableName, DenaObject... denaObjects) {
        return null;
    }

    @Override
    public List<DenaObject> replaceUpdate(String appName, String tableName, DenaObject... denaObjects) {
        return null;
    }

    @Override
    public long delete(String appName, String tableName, String... objectIds) {
        return 0;
    }

    @Override
    public long deleteRelation(String appName, String parentTableName, String parentObjectId, String childTableName, String childObjectId) {
        return 0;
    }

    @Override
    public long deleteRelation(String appName, String parentTableName, String parentObjectId, String relationName) {
        return 0;
    }

    @Override
    public List<DenaObject> find(String appName, String tableName, String... objectId) {
        List<DenaObject> results = new ArrayList<>(objectId.length);
        for (String id : objectId) {
            DenaObject object = new DenaObject();
            object.setObjectId(id);
            object.setObjectURI(tableName);
            results.add(object);
        }
        return results;
    }

    @Override
    public List<DenaObject> findAll(String appName, String tableName, DenaPager denaPager) {
        return null;
    }

    @Override
    public List<DenaObject> findRelatedObject(String appName, String parentTableName, String parentObjectId, String relationName, DenaPager denaPager) {
        return null;
    }
}
