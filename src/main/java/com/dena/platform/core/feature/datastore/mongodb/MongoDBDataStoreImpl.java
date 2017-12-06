package com.dena.platform.core.feature.datastore.mongodb;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.dto.RelatedObject;
import com.dena.platform.core.feature.datastore.DenaDataStore;
import com.dena.platform.core.feature.datastore.DenaPager;
import com.dena.platform.core.feature.datastore.exception.ObjectIdInvalidException;
import com.dena.platform.core.feature.datastore.exception.DataStoreException;
import com.dena.platform.common.exception.ErrorCode;
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
import java.util.stream.Collectors;

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
            throw new DataStoreException("Error in storing object", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
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
            return MongoDBUtils.deleteRelationWithObjectId(mongoDatabase, typeName1, objectId1, typeName2, objectId2);
        } catch (Exception ex) {
            throw new DataStoreException("Error in delete relation", ex);
        }
    }

    @Override
    public long deleteRelation(String appName, String typeName1, String objectId1, String typeName2) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteRelationWithType(mongoDatabase, typeName1, objectId1, typeName2);
        } catch (Exception ex) {
            throw new DataStoreException("Error in delete relation", ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public DenaObject findObject(String appName, String typeName, String objectId) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            DenaObject denaObject = new DenaObject();

            Document document = MongoDBUtils.findDocumentById(mongoDatabase, typeName, objectId);

            for (Map.Entry<String, Object> entry : document.entrySet()) {
                if (entry.getValue() instanceof ArrayList) {
                    if (((ArrayList) entry.getValue()).size() > 0 && ((ArrayList) entry.getValue()).get(0) instanceof ObjectId) {   // this type is relation
                        ArrayList<ObjectId> objectIdList = (ArrayList<ObjectId>) entry.getValue();
                        List<String> idString = objectIdList.stream()
                                .map(Object::toString)
                                .collect(Collectors.toList());

                        List<RelatedObject> relatedObjectList = convertToRelatedObject(entry.getKey(), idString);
                        denaObject.getRelatedObjects().addAll(relatedObjectList);
                    } else {
                        denaObject.addProperty(entry.getKey(), entry.getValue());  // this type is normal array
                    }
                } else if (entry.getKey().equals(MongoDBUtils.ID)) {
                    denaObject.setObjectId(entry.getValue().toString()); // type of id
                } else {
                    denaObject.addProperty(entry.getKey(), entry.getValue()); // normal key - value
                }


            }

            return denaObject;
        } catch (Exception ex) {
            throw new DataStoreException("Error in delete relation", ex);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public DenaObject findObjectRelation(String appName, String parentType, String objectId, String targetType, DenaPager denaPager) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            DenaObject denaObject = new DenaObject();

            Document parentDocument = MongoDBUtils.findDocumentById(mongoDatabase, parentType, objectId);
            List<Document> relatedDocuments = MongoDBUtils.findRelatedDocument(mongoDatabase, parentDocument, targetType, denaPager);

            relatedDocuments.forEach(document -> {
                for (Map.Entry<String, Object> entry : document.entrySet()) {
                    if (entry.getValue() instanceof ArrayList) {
                        if (((ArrayList) entry.getValue()).size() > 0 && ((ArrayList) entry.getValue()).get(0) instanceof ObjectId) {   // this type is relation
                            ArrayList<ObjectId> objectIdList = (ArrayList<ObjectId>) entry.getValue();
                            List<String> idString = objectIdList.stream()
                                    .map(Object::toString)
                                    .collect(Collectors.toList());

                            List<RelatedObject> relatedObjectList = convertToRelatedObject(entry.getKey(), idString);
                            denaObject.getRelatedObjects().addAll(relatedObjectList);
                        } else {
                            denaObject.addProperty(entry.getKey(), entry.getValue());  // this type is normal array
                        }
                    } else if (entry.getKey().equals(MongoDBUtils.ID)) {
                        denaObject.setObjectId(entry.getValue().toString()); // type of id
                    } else {
                        denaObject.addProperty(entry.getKey(), entry.getValue()); // normal key - value
                    }
                }
            });

            return denaObject;
        } catch (Exception ex) {
            throw new DataStoreException("Error in delete relation", ex);
        }
    }


    private void checkRelationValidity(MongoDatabase mongoDatabase, List<RelatedObject> relatedObjectList) {
        if (CollectionUtils.isNotEmpty(relatedObjectList)) {

            try {
                relatedObjectList.stream().allMatch(relatedObject ->
                        MongoDBUtils.findDocumentById(mongoDatabase, relatedObject.getTypeName(), relatedObject.getRelatedObjectId()) != null);
            } catch (IllegalArgumentException ex) {
                // in case of invalid object id, relation is invalid
                throw new DataStoreException("Relation(s) is invalid", ErrorCode.RELATION_INVALID_EXCEPTION, ex);
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

    private List<RelatedObject> convertToRelatedObject(String type, List<String> objectId) {

        List<RelatedObject> result = objectId.stream().map(id -> {
            RelatedObject relatedObject = new RelatedObject();
            relatedObject.setRelatedObjectId(id);
            relatedObject.setTypeName(type);
            return relatedObject;
        }).collect(Collectors.toList());

        return result;

    }

}
