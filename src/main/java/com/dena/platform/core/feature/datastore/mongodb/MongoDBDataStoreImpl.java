package com.dena.platform.core.feature.datastore.mongodb;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.dto.RelatedObject;
import com.dena.platform.core.feature.datastore.DenaDataStore;
import com.dena.platform.core.feature.datastore.exception.ObjectIdInvalidException;
import com.dena.platform.core.feature.datastore.exception.RelationInvalidException;
import com.dena.platform.core.feature.datastore.exception.DataStoreException;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@Service("denaMongoDBDataStoreImpl")
public class MongoDBDataStoreImpl implements DenaDataStore {
    private final static Logger log = LoggerFactory.getLogger(MongoDBDataStoreImpl.class);


    @Override
    public void storeObjects(final List<DenaObject> denaObjects, final String appName, final String typeName) throws DataStoreException {
        List<Document> documentList = new ArrayList<>();

        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);

            denaObjects.forEach(denaObject -> {

                checkRelationValidity(mongoDatabase, denaObject.getRelatedObjects());

                ObjectId objectId = ObjectId.get();
                denaObject.setObjectId(objectId.toHexString());

                Document document = new Document();
                document.put(MongoDBUtils.APP_NAME, appName);
                document.put(MongoDBUtils.TYPE_NAME, typeName);
                document.put(MongoDBUtils.ID, objectId);

                document.putAll(denaObject.getFields());

                // add relation
                if (CollectionUtils.isNotEmpty(denaObject.getRelatedObjects())) {
                    document.putAll(getRelation(denaObject));
                }
                documentList.add(document);
            });

            MongoDBUtils.createDocument(mongoDatabase, typeName, documentList);
        } catch (Exception ex) {
            throw new DataStoreException("Error in storing object", ex);
        }
    }

    @Override
    public void updateObjects(List<DenaObject> denaObjects, final String appName, final String typeName) throws DataStoreException {
        List<Document> documentList = new ArrayList<>();
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);

            denaObjects.forEach(denaObject -> {
                checkRelationValidity(mongoDatabase, denaObject.getRelatedObjects());
                checkObjectIdValidity(mongoDatabase, typeName, denaObject.getObjectId());
                if (StringUtils.isNoneBlank(denaObject.getObjectId())) {
                    ObjectId objectId = new ObjectId(denaObject.getObjectId());
                    Document document = new Document();
                    document.put(MongoDBUtils.APP_NAME, appName);
                    document.put(MongoDBUtils.TYPE_NAME, typeName);
                    document.put(MongoDBUtils.ID, objectId);

                    document.putAll(denaObject.getFields());

                    // update relation
                    if (CollectionUtils.isNotEmpty(denaObject.getRelatedObjects())) {
                        document.putAll(getRelation(denaObject));
                    }
                    documentList.add(document);
                } else {
                    log.error("ObjectId for [{}] is id", denaObject);
                }

            });

            MongoDBUtils.updateDocument(mongoDatabase, typeName, documentList);
        } catch (Exception ex) {
            throw new DataStoreException("Error in updating object", ex);
        }

    }

    @Override
    public long deleteObjects(String appName, String typeName, List<String> objectIds) throws DataStoreException {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteDocument(mongoDatabase, typeName, objectIds);
        } catch (Exception ex) {
            throw new DataStoreException("Error in deleting object", ex);
        }
    }

    @Override
    public long deleteRelation(String appName, String typeName1, String objectId1, String typeName2, String objectId2) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteRelation(mongoDatabase, typeName1, objectId1, typeName2, objectId2);
        } catch (Exception ex) {
            throw new DataStoreException("Error in delete relation", ex);
        }
    }

    @Override
    public long deleteRelation(String appName, String typeName1, String objectId1, String typeName2) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteRelation(mongoDatabase, typeName1, objectId1, typeName2);
        } catch (Exception ex) {
            throw new DataStoreException("Error in delete relation", ex);
        }
    }

    @Override
    public DenaObject findObject(String objectId) {
        return null;
    }


    private void checkRelationValidity(MongoDatabase mongoDatabase, List<RelatedObject> relatedObjectList) throws RelationInvalidException {
        if (CollectionUtils.isNotEmpty(relatedObjectList)) {
            boolean isRelationValid;

            try {
                isRelationValid = relatedObjectList.stream().allMatch(relatedObject ->
                        MongoDBUtils.findDocumentById(mongoDatabase, relatedObject.getTypeName(), relatedObject.getRelatedObjectId()) != null);
            } catch (IllegalArgumentException ex) {
                // in case of invalid object id, relation is invalid
                isRelationValid = false;
            }

            if (!isRelationValid) {
                throw new RelationInvalidException("Relation(s) is invalid");
            }
        }

    }

    private void checkObjectIdValidity(MongoDatabase mongoDatabase, String typeName, String objectId) throws ObjectIdInvalidException {
        boolean isObjectIdValid;
        try {

            isObjectIdValid = MongoDBUtils.findDocumentById(mongoDatabase, typeName, objectId) != null;
        } catch (IllegalArgumentException ex) {
            // in case of invalid object id, relation is invalid
            isObjectIdValid = false;
        }

        if (!isObjectIdValid) {
            throw new ObjectIdInvalidException("ObjectId invalid exception");
        }
    }

    private Map<String, List<ObjectId>> getRelation(DenaObject denaObject) {
        Map<String, List<ObjectId>> references = new HashMap<>();

        denaObject
                .getRelatedObjects()
                .forEach(relatedObject -> {
                    references.putIfAbsent(relatedObject.getTypeName(), new ArrayList<>());
                    ObjectId objectId = new ObjectId(relatedObject.getRelatedObjectId());
                    references.get(relatedObject.getTypeName()).add(objectId);
                });

        return references;
    }

}
