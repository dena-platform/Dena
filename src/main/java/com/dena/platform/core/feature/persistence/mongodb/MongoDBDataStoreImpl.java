package com.dena.platform.core.feature.persistence.mongodb;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.dto.RelatedObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.persistence.exception.DataStoreException;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@Service("denaMongoDBDataStoreImpl")
public class MongoDBDataStoreImpl implements DenaDataStore {
    private final static Logger log = LoggerFactory.getLogger(MongoDBDataStoreImpl.class);


    @Override
    public List<DenaObject> storeObjects(final List<DenaObject> denaObjects, final String appName, final String typeName) {
        List<Document> documentList = new ArrayList<>();
        List<DenaObject> returnObject = new ArrayList<>();
        MongoDatabase mongoDatabase;

        if (CollectionUtils.isEmpty(denaObjects)) {
            return Collections.emptyList();
        }

        try {
            mongoDatabase = MongoDBUtils.getDataBase(appName);
        } catch (Exception ex) {
            throw new DataStoreException("Error in updating objects", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }


        denaObjects.forEach(denaObject -> {
            checkRelationValidity(mongoDatabase, denaObject.getRelatedObjects());
        });

        List<String> ids = new ArrayList<>();
        try {

            denaObjects.forEach(denaObject -> {

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
                ids.add(objectId.toString());
            });

            MongoDBUtils.createDocument(mongoDatabase, typeName, documentList);

            // todo : performance- use better approach to find object with ids (bulk find)
            ids.forEach(id -> {
                returnObject.add(findObject(appName, typeName, id));
            });

            return returnObject;
        } catch (Exception ex) {
            throw new DataStoreException("Error in storing object", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }

    @Override
    public List<DenaObject> updateObjects(List<DenaObject> denaObjects, final String appName, final String typeName) {
        List<DenaObject> returnObject = new ArrayList<>();
        MongoDatabase mongoDatabase;
        List<Document> documentList = new ArrayList<>();

        if (CollectionUtils.isEmpty(denaObjects)) {
            return Collections.emptyList();
        }

        try {
            mongoDatabase = MongoDBUtils.getDataBase(appName);
        } catch (Exception ex) {
            throw new DataStoreException("Error in updating objects", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }

        denaObjects.forEach(denaObject -> {
            checkRelationValidity(mongoDatabase, denaObject.getRelatedObjects());
            checkObjectIdExist(mongoDatabase, typeName, denaObject.getObjectId());
        });

        List<String> ids = new ArrayList<>();
        try {
            denaObjects.forEach(denaObject -> {
                ObjectId objectId = new ObjectId(denaObject.getObjectId());
                Document document = new Document();
                document.put(MongoDBUtils.ID, objectId);
                document.putAll(denaObject.getFields());

                // update relation
                if (CollectionUtils.isNotEmpty(denaObject.getRelatedObjects())) {
                    document.putAll(getRelation(denaObject));
                }
                documentList.add(document);
                ids.add(objectId.toString());
            });

            MongoDBUtils.updateDocument(mongoDatabase, typeName, documentList);

            // todo : performance- use better approach to find object with ids (bulk find)
            ids.forEach(id -> {
                returnObject.add(findObject(appName, typeName, id));
            });
            return returnObject;
        } catch (Exception ex) {
            throw new DataStoreException("Error in updating objects", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }

    }

    @Override
    public long deleteObjects(String appName, String typeName, List<String> objectIds) {
        checkObjectIdValidity(objectIds);
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteDocument(mongoDatabase, typeName, objectIds);
        } catch (Exception ex) {
            throw new DataStoreException("Error in deleting object", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }

    }

    @Override
    public long deleteRelation(String appName, String parentTypeName, String parentObjectId, String childTypeName, String childObjectId) {
        checkObjectIdValidity(Arrays.asList(parentObjectId, childObjectId));
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteRelationWithObjectId(mongoDatabase, parentTypeName, parentObjectId, childTypeName, childObjectId);
        } catch (Exception ex) {
            throw new DataStoreException("Error in delete relation", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }

    @Override
    public long deleteRelation(String appName, String parentTypeName, String parentObjectId, String childTypeName) {
        checkObjectIdValidity(Collections.singletonList(parentObjectId));

        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteRelationWithType(mongoDatabase, parentTypeName, parentObjectId, childTypeName);
        } catch (Exception ex) {
            throw new DataStoreException("Error in delete relation", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public DenaObject findObject(String appName, String typeName, String objectId) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            DenaObject denaObject = new DenaObject();

            Optional<Document> document = MongoDBUtils.findDocumentById(mongoDatabase, typeName, objectId);
            if (!document.isPresent()) {
                return null;
            }

            for (Map.Entry<String, Object> entry : document.get().entrySet()) {
                if (entry.getValue() instanceof ArrayList) {
                    // is type relation?
                    if (((ArrayList) entry.getValue()).size() > 0 && ((ArrayList) entry.getValue()).get(0) instanceof ObjectId) {
                        ArrayList<ObjectId> objectIdList = (ArrayList<ObjectId>) entry.getValue();
                        List<String> idString = objectIdList.stream()
                                .map(Object::toString)
                                .collect(Collectors.toList());

                        List<RelatedObject> relatedObjectList = convertToRelatedObject(entry.getKey(), idString);
                        denaObject.setRelatedObjects(relatedObjectList);
                    }
                    // this type is normal array
                    else {
                        denaObject.addProperty(entry.getKey(), entry.getValue());
                    }
                } else if (entry.getKey().equals(MongoDBUtils.ID)) {  // type is id
                    denaObject.setObjectId(entry.getValue().toString());
                } else { // normal key -> value
                    denaObject.addProperty(entry.getKey(), entry.getValue());
                }
            }

            return denaObject;
        } catch (Exception ex) {
            throw new DataStoreException("Error in finding object", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<DenaObject> findObjectRelation(String appName, String parentType, String objectId, String targetType, DenaPager denaPager) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            Optional<Document> parentDocument = MongoDBUtils.findDocumentById(mongoDatabase, parentType, objectId);

            if (!parentDocument.isPresent()) {
                return null;
            }

            List<DenaObject> denaObjects = new ArrayList<>();
            List<Document> relatedDocuments = MongoDBUtils.findRelatedDocument(mongoDatabase, parentDocument.get(), targetType, denaPager);

            relatedDocuments.forEach(document -> {
                DenaObject denaObject = new DenaObject();
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

                denaObjects.add(denaObject);
            });

            return denaObjects;
        } catch (Exception ex) {
            throw new DataStoreException("Error in finding relation object", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }


    private void checkRelationValidity(MongoDatabase mongoDatabase, List<RelatedObject> relatedObjectList) {

        if (CollectionUtils.isNotEmpty(relatedObjectList)) {
            boolean isObjectIdValid;
            // todo: use count to check relation validity for performance reason
            try {
                isObjectIdValid = relatedObjectList.stream().allMatch(relatedObject ->
                        MongoDBUtils.findDocumentById(mongoDatabase, relatedObject.getTypeName(), relatedObject.getRelatedObjectId()).isPresent());
            } catch (IllegalArgumentException ex) {
                // in case of invalid object id, relation is invalid
                isObjectIdValid = false;
            }

            if (!isObjectIdValid) {
                throw new DataStoreException("Relation(s) is invalid", ErrorCode.RELATION_INVALID_EXCEPTION);
            }
        }

    }

    private void checkObjectIdExist(MongoDatabase mongoDatabase, String typeName, String objectId) {
        checkObjectIdValidity(Collections.singletonList(objectId));

        boolean isObjectIdValid = MongoDBUtils.findDocumentById(mongoDatabase, typeName, objectId).isPresent();

        if (!isObjectIdValid) {
            throw new DataStoreException("ObjectId not found exception", ErrorCode.ObjectId_NOT_FOUND_EXCEPTION);
        }
    }

    private void checkObjectIdValidity(List<String> objectId) {
        boolean isParameterValid;

        try {
            isParameterValid = objectId.stream().allMatch(ObjectId::isValid);
        } catch (IllegalArgumentException ex) {
            isParameterValid = false;
        }

        if (!isParameterValid) {
            throw new DataStoreException("ObjectId invalid exception", ErrorCode.ObjectId_INVALID_EXCEPTION);
        }
    }

    private Map<String, List<ObjectId>> getRelation(DenaObject denaObject) {
        Map<String, List<ObjectId>> references = new HashMap<>();

        denaObject.getRelatedObjects()
                .forEach(relatedObject -> {
                    references.putIfAbsent(relatedObject.getTypeName(), new ArrayList<>());
                    ObjectId objectId = new ObjectId(relatedObject.getRelatedObjectId());
                    references.get(relatedObject.getTypeName()).add(objectId);
                });

        return references;
    }

    private List<RelatedObject> convertToRelatedObject(String type, List<String> objectIdList) {

        List<RelatedObject> result = objectIdList.stream().map(id -> {
            RelatedObject relatedObject = new RelatedObject();
            relatedObject.setRelatedObjectId(id);
            relatedObject.setTypeName(type);
            return relatedObject;
        }).collect(Collectors.toList());

        return result;

    }

}
