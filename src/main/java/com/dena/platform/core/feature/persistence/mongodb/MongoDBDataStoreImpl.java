package com.dena.platform.core.feature.persistence.mongodb;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.common.utils.DenaObjectUtils;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.dto.RelatedObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.persistence.exception.DataStoreException;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.bson.*;
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
        List<BsonDocument> bsonDocuments = new ArrayList<>();
        List<DenaObject> returnObject = new ArrayList<>();
        MongoDatabase mongoDatabase;

        if (CollectionUtils.isEmpty(denaObjects)) {
            return Collections.emptyList();
        }

        try {
            mongoDatabase = MongoDBUtils.getDataBase(appName);
        } catch (Exception ex) {
            throw new DataStoreException("Error in creating objects", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }

        denaObjects.forEach(denaObject -> {
            checkRelationValidity(mongoDatabase, denaObject.getRelatedObjects());
        });

        List<String> ids = new ArrayList<>();
        try {

            denaObjects.forEach(denaObject -> {

                ObjectId objectId = ObjectId.get();

                BsonDocument bsonDocument = new BsonDocument();
                bsonDocument.put(MongoDBUtils.ID, new BsonObjectId(objectId));
                bsonDocument.put(CREATE_TIME_FIELD, new BsonDateTime(DenaObjectUtils.timeStamp()));
                bsonDocument.put(UPDATE_TIME_FIELD, new BsonNull());
                bsonDocument.put(OBJECT_URI_FIELD, new BsonString(DenaObjectUtils.getURIForResource(typeName, objectId.toString())));
                addFieldsToBsonDocument(bsonDocument, denaObject.getFields());

                // add relation
                if (CollectionUtils.isNotEmpty(denaObject.getRelatedObjects())) {
                    bsonDocument.putAll(getRelation(denaObject));
                }

                bsonDocuments.add(bsonDocument);
                ids.add(objectId.toString());
            });

            MongoDBUtils.createDocument(mongoDatabase, typeName, bsonDocuments);

            // todo : performance - use better approach to find object with ids (bulk find)
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
            throw new DataStoreException("Error in deleting relation", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
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

    @Override
    public DenaObject findObject(String appName, String typeName, String objectId) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            DenaObject denaObject = new DenaObject();

            Optional<BsonDocument> bsonDocument = MongoDBUtils.findDocumentById(mongoDatabase, typeName, objectId);
            if (!bsonDocument.isPresent()) {
                return null;
            }

            for (Map.Entry<String, BsonValue> entry : bsonDocument.get().entrySet()) {
                String fieldName = entry.getKey();
                BsonValue fieldValue = entry.getValue();

                if (fieldValue.isArray()) {
                    BsonArray values = fieldValue.asArray();

                    if (values.size() > 0 && values.get(0).isObjectId()) {
                        // this type is relation?
                        log.trace("We ignore [{}]. because it is relation type.", fieldName);
                    } else {
                        // this type is normal array
                        ArrayList<Object> listOfObject = BsonTypeMapper.convertBSONArrayToJavaArray(values);
                        denaObject.addProperty(fieldName, listOfObject);
                    }
                } else if (fieldName.equals(MongoDBUtils.ID)) {  // type is id field
                    denaObject.setObjectId(fieldValue.asObjectId().getValue().toString());
                } else if (fieldName.equals(UPDATE_TIME_FIELD)) {  // type is update_time field
                    denaObject.setUpdateTime(fieldValue.isNull() ? null : fieldValue.asDateTime().getValue());
                } else if (fieldName.equals(CREATE_TIME_FIELD)) {  // type is create_time field
                    denaObject.setCreateTime(fieldValue.isNull() ? null : fieldValue.asDateTime().getValue());
                } else if (fieldName.equals(OBJECT_URI_FIELD)) {  // type is uri field
                    denaObject.setObjectURI(fieldValue.asString().getValue());
                } else { // normal key -> value
                    denaObject.addProperty(fieldName, BsonTypeMapper.convertBSONToJava(fieldValue));
                }
            }

            return denaObject;
        } catch (Exception ex) {
            throw new DataStoreException("Error in finding object", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }


    @Override
    public List<DenaObject> findObjectRelation(String appName, String parentType, String objectId, String targetType, DenaPager denaPager) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            Optional<BsonDocument> parentDocument = MongoDBUtils.findDocumentById(mongoDatabase, parentType, objectId);

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
                isObjectIdValid = relatedObjectList
                        .stream()
                        .allMatch(relatedObject -> {
                            // check if target type is exist
                            boolean isCollectionExist = MongoDBUtils.isCollectionExist(mongoDatabase, relatedObject.getRelationName());
                            boolean isDocumentsExist = MongoDBUtils
                                    .findDocumentById(mongoDatabase, relatedObject.getTargetName(), relatedObject.getIds()).size() == relatedObject.getIds().size();

                            return isCollectionExist && isDocumentsExist;
                        });


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
            throw new DataStoreException(String.format("ObjectId [%s} invalid exception", objectId), ErrorCode.ObjectId_INVALID_EXCEPTION);
        }
    }

    private Map<String, BsonValue> getRelation(DenaObject denaObject) {
        Map<String, BsonValue> references = new HashMap<>();

        denaObject.getRelatedObjects()
                .forEach(relatedObject -> {
                    BsonDocument relation = new BsonDocument();
                    relation.put(MongoDBUtils.RELATED_TARGET_NAME, new BsonString(relatedObject.getTargetName()));
                    relation.put(MongoDBUtils.RELATED_IDS, BsonTypeMapper.convertObjectToBSONValue(relatedObject.getIds()));

                    references.put(relatedObject.getRelationName(), relation);

                });

        return references;
    }

    private List<RelatedObject> convertToRelatedObject(String type, List<String> objectIdList) {

        List<RelatedObject> result = objectIdList.stream().map(id -> {
            RelatedObject relatedObject = new RelatedObject();
            relatedObject.setIds(objectIdList);
            relatedObject.setTargetName(type);
            return relatedObject;
        }).collect(Collectors.toList());

        return result;

    }

    /**
     * Recognize field value type and add to bson document
     *
     * @param bsonDocument
     * @param properties
     */
    private void addFieldsToBsonDocument(BsonDocument bsonDocument, Map<String, Object> properties) {
        if (MapUtils.isNotEmpty(properties)) {
            properties.forEach((fieldName, fieldValue) -> {
                BsonValue bsonValue = BsonTypeMapper.convertObjectToBSONValue(fieldValue);
                bsonDocument.put(fieldName, bsonValue);
            });
        }
    }

}
