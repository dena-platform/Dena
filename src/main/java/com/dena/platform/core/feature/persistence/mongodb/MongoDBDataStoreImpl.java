package com.dena.platform.core.feature.persistence.mongodb;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.common.utils.DenaObjectUtils;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.dto.DenaRelation;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.persistence.RelationType;
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


        List<String> ids = new ArrayList<>();
        try {
            mongoDatabase = MongoDBUtils.getDataBase(appName);
            denaObjects.forEach(denaObject -> {

                ObjectId objectId = ObjectId.get();

                BsonDocument bsonDocument = new BsonDocument();
                bsonDocument.put(MongoDBUtils.ID, new BsonObjectId(objectId));
                bsonDocument.put(MongoDBUtils.CREATE_TIME_FIELD, new BsonDateTime(DenaObjectUtils.timeStamp()));
                bsonDocument.put(MongoDBUtils.UPDATE_TIME_FIELD, new BsonNull());
                bsonDocument.put(MongoDBUtils.OBJECT_URI_FIELD, new BsonString(DenaObjectUtils.getURIForResource(typeName, objectId.toString())));
                addFieldsToBsonDocument(bsonDocument, denaObject.getFields());

                // add relation
                if (CollectionUtils.isNotEmpty(denaObject.getDenaRelations())) {
                    checkRelationValidity(mongoDatabase, denaObject.getDenaRelations());
                    bsonDocument.putAll(getRelation(denaObject));
                }

                bsonDocuments.add(bsonDocument);
                ids.add(objectId.toString());
            });

            MongoDBUtils.createDocuments(mongoDatabase, typeName, bsonDocuments.toArray(new BsonDocument[bsonDocuments.size()]));

            // todo : performance - use better approach to find object with ids (bulk find)
            ids.forEach(id -> returnObject.add(findObject(appName, typeName, id)));

            return returnObject;
        } catch (DataStoreException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataStoreException("Error in storing object(s)", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
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
            checkRelationValidity(mongoDatabase, denaObject.getDenaRelations());
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
                if (CollectionUtils.isNotEmpty(denaObject.getDenaRelations())) {
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

                if (fieldValue.isDocument() && fieldValue.asDocument().containsKey(MongoDBUtils.RELATION_TYPE)) {
                    // this field is relation
                    BsonDocument relatedDocument = fieldValue.asDocument();

                    BsonArray idArray = relatedDocument.getArray(MongoDBUtils.RELATION_IDS);
                    List<String> idStringArray = BsonTypeMapper.convertBSONArrayToJavaArray(idArray)
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.toList());

                    String relationTargetName = relatedDocument.getString(MongoDBUtils.RELATION_TARGET_NAME).getValue();
                    String relationTypeName = relatedDocument.getString(MongoDBUtils.RELATION_TYPE).getValue();


                    DenaRelation denaRelation = new DenaRelation();
                    denaRelation.setIds(idStringArray);
                    denaRelation.setType(relationTypeName);
                    denaRelation.setTargetName(relationTargetName);

                    denaObject.addRelatedObjects(denaRelation);

                } else if (fieldValue.isArray()) {
                    BsonArray values = fieldValue.asArray();
                    // this field is normal array
                    ArrayList<Object> listOfObject = BsonTypeMapper.convertBSONArrayToJavaArray(values);
                    denaObject.addProperty(fieldName, listOfObject);
                } else if (fieldName.equals(MongoDBUtils.ID)) {  // type is id field
                    denaObject.setObjectId(fieldValue.asObjectId().getValue().toString());
                } else if (fieldName.equals(MongoDBUtils.UPDATE_TIME_FIELD)) {  // type is update_time field
                    denaObject.setUpdateTime(fieldValue.isNull() ? null : fieldValue.asDateTime().getValue());
                } else if (fieldName.equals(MongoDBUtils.CREATE_TIME_FIELD)) {  // type is create_time field
                    denaObject.setCreateTime(fieldValue.isNull() ? null : fieldValue.asDateTime().getValue());
                } else if (fieldName.equals(MongoDBUtils.OBJECT_URI_FIELD)) {  // type is uri field
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

                            List<DenaRelation> denaRelationList = convertToRelatedObject(entry.getKey(), idString);
                            denaObject.getDenaRelations().addAll(denaRelationList);
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


    private void checkRelationValidity(MongoDatabase mongoDatabase, List<DenaRelation> denaRelationList) {

        if (CollectionUtils.isNotEmpty(denaRelationList)) {
            log.debug("Check validity for relation [{}]", denaRelationList);
            boolean isObjectIdValid;
            // todo: use count to check relation validity for performance reason
            try {
                isObjectIdValid = denaRelationList
                        .stream()
                        .allMatch(denaRelation -> {
                            // check if target type is exist
                            boolean isCollectionExist = MongoDBUtils.isCollectionExist(mongoDatabase, denaRelation.getTargetName());
                            boolean isDocumentsExist = MongoDBUtils
                                    .findDocumentById(mongoDatabase, denaRelation.getTargetName(), denaRelation.getIds()).size() == denaRelation.getIds().size();

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
            throw new DataStoreException(String.format("ObjectId [%s] invalid exception", objectId), ErrorCode.ObjectId_INVALID_EXCEPTION);
        }
    }

    private Map<String, BsonValue> getRelation(DenaObject denaObject) {
        Map<String, BsonValue> relations = new HashMap<>();

        denaObject.getDenaRelations()
                .forEach(denaRelation -> {
                    BsonDocument relation = new BsonDocument();
                    relation.put(MongoDBUtils.RELATION_TARGET_NAME, new BsonString(denaRelation.getTargetName()));

                    List<ObjectId> objectIds = denaRelation.getIds()
                            .stream()
                            .map(ObjectId::new)
                            .collect(Collectors.toList());
                    relation.put(MongoDBUtils.RELATION_IDS, BsonTypeMapper.convertObjectToBSONValue(objectIds));

                    relation.put(MongoDBUtils.RELATION_TYPE, new BsonString(RelationType.RELATION_1_TO_1.value));

                    relations.put(denaRelation.getRelationName(), relation);

                });

        return relations;
    }

    private List<DenaRelation> convertToRelatedObject(String type, List<String> objectIdList) {

        List<DenaRelation> result = objectIdList.stream().map(id -> {
            DenaRelation denaRelation = new DenaRelation();
            denaRelation.setIds(objectIdList);
            denaRelation.setTargetName(type);
            return denaRelation;
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
