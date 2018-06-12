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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.*;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

@Service("denaMongoDBDataStore")
public class MongoDBDataStoreImpl implements DenaDataStore {
    private final static Logger log = LoggerFactory.getLogger(MongoDBDataStoreImpl.class);


    @Override
    public List<DenaObject> store(String appName, String tableName, DenaObject... denaObjects) {
        List<BsonDocument> bsonDocuments = new ArrayList<>();
        MongoDatabase mongoDatabase;

        if (ArrayUtils.isEmpty(denaObjects)) {
            return Collections.emptyList();
        }


        List<String> ids = new ArrayList<>();
        try {
            mongoDatabase = MongoDBUtils.getDataBase(appName);
            for (DenaObject denaObject : denaObjects) {

                ObjectId objectId = ObjectId.get();

                BsonDocument bsonDocument = new BsonDocument();
                bsonDocument.put(MongoDBUtils.ID, new BsonObjectId(objectId));
                bsonDocument.put(MongoDBUtils.CREATE_TIME_FIELD, new BsonDateTime(DenaObjectUtils.timeStamp()));
                bsonDocument.put(MongoDBUtils.UPDATE_TIME_FIELD, new BsonNull());
                bsonDocument.put(MongoDBUtils.OBJECT_URI_FIELD, new BsonString(DenaObjectUtils.getURIForResource(tableName, objectId.toString())));
                addFieldsToBsonDocument(bsonDocument, denaObject.getOtherFields());

                // add relation
                if (CollectionUtils.isNotEmpty(denaObject.getDenaRelations())) {
                    checkRelationValidity(mongoDatabase, denaObject, denaObject.getDenaRelations());
                    bsonDocument.putAll(getRelation(denaObject));
                }

                bsonDocuments.add(bsonDocument);
                ids.add(objectId.toString());
            }

            MongoDBUtils.createDocuments(mongoDatabase, tableName, bsonDocuments.toArray(new BsonDocument[0]));

            return new ArrayList<>(find(appName, tableName, ids.toArray(new String[0])));
        } catch (DataStoreException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataStoreException("Error in storing object(s)", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }

    @Override
    public List<DenaObject> mergeUpdate(final String appName, final String tableName, DenaObject... denaObjects) {
        log.info("Merging objects [{}]", (Object[]) denaObjects);

        MongoDatabase mongoDatabase;
        List<BsonDocument> bsonDocumentList = new ArrayList<>();

        if (ArrayUtils.isEmpty(denaObjects)) {
            log.debug("Object request list is empty");
            return Collections.emptyList();
        }

        try {
            mongoDatabase = MongoDBUtils.getDataBase(appName);

            for (DenaObject denaObject : denaObjects) {
                checkIfObjectIdExist(mongoDatabase, tableName, denaObject.getObjectId());
                checkRelationValidity(mongoDatabase, denaObject, denaObject.getDenaRelations());
            }

            List<String> ids = new LinkedList<>();

            for (DenaObject denaObject : denaObjects) {
                ObjectId objectId = new ObjectId(denaObject.getObjectId());
                BsonDocument bsonDocument = new BsonDocument();
                bsonDocument.put(MongoDBUtils.ID, new BsonObjectId(objectId));
                bsonDocument.put(MongoDBUtils.UPDATE_TIME_FIELD, new BsonDateTime(DenaObjectUtils.timeStamp()));


                addFieldsToBsonDocument(bsonDocument, denaObject.getOtherFields());

                // mergeUpdate relation
                if (CollectionUtils.isNotEmpty(denaObject.getDenaRelations())) {
                    DenaObject existingDenaObject = find(appName, tableName, objectId.toString()).get(0);
                    List<DenaRelation> existingDenaRelations = existingDenaObject.getDenaRelations();
                    List<DenaRelation> requestRelations = denaObject.getDenaRelations();
                    List<DenaRelation> resultRelation = new LinkedList<>();

                    for (DenaRelation requestRelation : requestRelations) {
                        int foundIndex = existingDenaRelations.indexOf(requestRelation);
                        if (foundIndex > -1) {
                            // relation exist in data store, check if id exist
                            List<String> existingIdForRelation = existingDenaRelations.get(foundIndex).getIds();
                            List<String> requestingIdForRelation = requestRelation.getIds();


                            if (!existingIdForRelation.containsAll(requestingIdForRelation)) {
                                log.debug("Adding new relation ids {}", requestingIdForRelation);
                                // there is new object id in relation, so add it.

                                List<String> resultIds = new ArrayList<>(CollectionUtils.union(existingIdForRelation, requestingIdForRelation));
                                requestRelation.setIds(resultIds);
                                resultRelation.add(requestRelation);
                            }
                        } else {
                            // this is new relation add it to result
                            log.debug("Adding new relation {}", requestRelation);
                            resultRelation.add(requestRelation);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(resultRelation)) {
                        denaObject.setDenaRelations(resultRelation);
                        bsonDocument.putAll(getRelation(denaObject));
                    }
                }

                bsonDocumentList.add(bsonDocument);
                ids.add(objectId.toString());
            }


            MongoDBUtils.updateDocument(mongoDatabase, tableName, bsonDocumentList.toArray(new BsonDocument[0]));

            return new ArrayList<>(find(appName, tableName, ids.toArray(new String[0])));
        } catch (DataStoreException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataStoreException("Error in updating object(s)", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }

    }

    @Override
    public List<DenaObject> replaceUpdate(String appName, String tableName, DenaObject... denaObjects) {
        log.info("Updating objects [{}]", (Object[]) denaObjects);

        MongoDatabase mongoDatabase;
        List<BsonDocument> bsonDocumentList = new ArrayList<>();

        if (ArrayUtils.isEmpty(denaObjects)) {
            log.debug("Object request list is empty");
            return Collections.emptyList();
        }

        try {
            mongoDatabase = MongoDBUtils.getDataBase(appName);
            for (DenaObject denaObject : denaObjects) {
                checkIfObjectIdExist(mongoDatabase, tableName, denaObject.getObjectId());
                checkRelationValidity(mongoDatabase, denaObject, denaObject.getDenaRelations());
            }


        }


        return null;
    }

    @Override
    public long delete(String appName, String tableName, String... objectIds) {
        if (ArrayUtils.isEmpty(objectIds)) {
            return 0;
        }

        checkObjectIdValidity(objectIds);
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteDocument(mongoDatabase, tableName, objectIds);
        } catch (Exception ex) {
            throw new DataStoreException("Error in deleting object(s)", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }

    }

    @Override
    public long deleteRelation(String appName, String parentTableName, String parentObjectId, String childTableName, String childObjectId) {
        checkObjectIdValidity(parentObjectId, childObjectId);
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteRelationWithObjectId(mongoDatabase, parentTableName, parentObjectId, childTableName, childObjectId);
        } catch (Exception ex) {
            throw new DataStoreException("Error in deleting relation", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }

    @Override
    public long deleteRelation(String appName, String parentTableName, String parentObjectId, String relationName) {
        checkObjectIdValidity(parentObjectId);
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteRelationWithType(mongoDatabase, parentTableName, parentObjectId, relationName);
        } catch (Exception ex) {
            throw new DataStoreException("Error in deleting relation", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }

    @Override
    public List<DenaObject> find(String appName, String tableName, String... objectId) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);

            List<BsonDocument> foundDocuments = MongoDBUtils.findDocumentById(mongoDatabase, tableName, objectId);

            return convertBsonDocumentsToDenaObjects(foundDocuments);
        } catch (Exception ex) {
            throw new DataStoreException("Error in finding object", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }

    @Override
    public List<DenaObject> findAll(String appName, String tableName, DenaPager denaPager) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);

            List<BsonDocument> foundDocuments = MongoDBUtils.findALLDocument(mongoDatabase, tableName, denaPager);

            return convertBsonDocumentsToDenaObjects(foundDocuments);
        } catch (Exception ex) {
            throw new DataStoreException("Error in finding object(s)", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }

    @Override
    public List<DenaObject> findRelatedObject(String appName, String parentTableName, String parentObjectId, String relationName, DenaPager denaPager) {
        try {
            checkObjectIdValidity(parentObjectId);
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            List<BsonDocument> parentDocument = MongoDBUtils.findDocumentById(mongoDatabase, parentTableName, parentObjectId);

            if (CollectionUtils.isEmpty(parentDocument)) {
                return Collections.emptyList();
            }

            List<BsonDocument> foundDocuments = MongoDBUtils.findRelatedDocument(mongoDatabase, parentDocument.get(0), relationName, denaPager);

            return convertBsonDocumentsToDenaObjects(foundDocuments);

        } catch (Exception ex) {
            throw new DataStoreException("Error in finding related object(s)", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }


    private void checkRelationValidity(MongoDatabase mongoDatabase, DenaObject parentObject, List<DenaRelation> relationList) {

        if (CollectionUtils.isNotEmpty(relationList)) {
            log.debug("Check validity for relation(s) [{}]", relationList);


            if (StringUtils.isNotEmpty(parentObject.getObjectId())) {
                // This is an mergeUpdate operation
                // Check if target for relation name matching existing relation name
                relationList.forEach(denaRelation -> {
                    int indexOfRelation = parentObject.getDenaRelations().indexOf(denaRelation);
                    if (indexOfRelation > -1) {
                        if (!parentObject.getDenaRelations().get(indexOfRelation).getTargetName().equals(denaRelation.getTargetName())) {
                            log.debug("Target name [{}] not compatible with [{}]", denaRelation, parentObject.getDenaRelations().indexOf(denaRelation));
                            throw new DataStoreException("Relation(s) is invalid", ErrorCode.RELATION_INVALID_EXCEPTION);
                        }
                    }
                });
            }

            boolean isObjectIdValid;
            try {
                isObjectIdValid = relationList
                        .stream()
                        .allMatch(denaRelation -> {
                            // check if target type is exist
                            boolean isCollectionExist = MongoDBUtils.isSchemaExist(mongoDatabase, denaRelation.getTargetName());
                            String[] ids = denaRelation.getIds().toArray(new String[0]);
                            boolean isIdsExist = ids.length > 0 &&
                                    (MongoDBUtils.findDocumentById(mongoDatabase, denaRelation.getTargetName(), ids)
                                            .size() == denaRelation.getIds().size());

                            if (!isCollectionExist) {
                                log.debug("Type [{}] dose not exist", denaRelation.getTargetName());
                            }

                            if (!isIdsExist) {
                                log.debug("Relation ids [{}] dose not exist", (Object[]) ids);
                            }
                            return isCollectionExist && isIdsExist;
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

    private void checkIfObjectIdExist(MongoDatabase mongoDatabase, String typeName, String objectId) {
        checkObjectIdValidity(objectId);

        boolean isObjectIdExist = CollectionUtils.isNotEmpty(MongoDBUtils.findDocumentById(mongoDatabase, typeName, objectId));

        if (!isObjectIdExist) {
            throw new DataStoreException("ObjectId not found exception", ErrorCode.OBJECT_ID_NOT_FOUND_EXCEPTION);
        }
    }

    private void checkObjectIdValidity(String... objectIds) {
        if (ArrayUtils.isNotEmpty(objectIds)) {
            boolean isParameterValid = true;


            for (String objectId : objectIds) {
                try {
                    if (!ObjectId.isValid(objectId)) {
                        isParameterValid = false;
                        break;
                    }
                } catch (IllegalArgumentException ex) {
                    isParameterValid = false;
                }
            }

            if (!isParameterValid) {
                throw new DataStoreException(String.format("ObjectId [%s] invalid exception", (Object[]) objectIds), ErrorCode.OBJECT_ID_INVALID_EXCEPTION);
            }

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
                    relation.put(MongoDBUtils.RELATION_IDS, BSONTypeMapper.convertObjectToBSONValue(objectIds));

                    relation.put(MongoDBUtils.RELATION_TYPE, new BsonString(RelationType.RELATION_1_TO_1.value));

                    relations.put(denaRelation.getRelationName(), relation);

                });

        return relations;
    }

    private List<DenaObject> convertBsonDocumentsToDenaObjects(List<BsonDocument> bsonDocuments) {
        List<DenaObject> result = new LinkedList<>();
        if (CollectionUtils.isEmpty(bsonDocuments)) {
            return Collections.emptyList();
        }

        bsonDocuments.forEach(bsonDocument -> {
            DenaObject denaObject = new DenaObject();
            for (Map.Entry<String, BsonValue> entry : bsonDocument.entrySet()) {
                String fieldName = entry.getKey();
                BsonValue fieldValue = entry.getValue();

                if (fieldValue.isDocument() && fieldValue.asDocument().containsKey(MongoDBUtils.RELATION_TYPE)) {
                    // this field is relation
                    BsonDocument relatedDocument = fieldValue.asDocument();

                    BsonArray idArray = relatedDocument.getArray(MongoDBUtils.RELATION_IDS);
                    List<String> idStringArray = BSONTypeMapper.convertBSONArrayToJava(idArray)
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.toList());

                    String relationTargetName = relatedDocument.getString(MongoDBUtils.RELATION_TARGET_NAME).getValue();
                    String relationTypeName = relatedDocument.getString(MongoDBUtils.RELATION_TYPE).getValue();


                    DenaRelation denaRelation = new DenaRelation();
                    denaRelation.setIds(idStringArray);
                    denaRelation.setRelationType(relationTypeName);
                    denaRelation.setTargetName(relationTargetName);
                    denaRelation.setRelationName(fieldName);
                    denaObject.addRelatedObjects(denaRelation);

                } else if (fieldValue.isArray()) {
                    BsonArray values = fieldValue.asArray();
                    // this field is normal array
                    ArrayList<Object> listOfObject = BSONTypeMapper.convertBSONArrayToJava(values);
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
                    denaObject.addProperty(fieldName, BSONTypeMapper.convertBSONToJava(fieldValue));
                }
            }
            result.add(denaObject);
        });

        return result;

    }

    /**
     * Recognize field type and add to bson document
     *
     * @param bsonDocument
     * @param properties
     */
    private void addFieldsToBsonDocument(BsonDocument bsonDocument, Map<String, Object> properties) {
        if (MapUtils.isNotEmpty(properties)) {
            properties.forEach((fieldName, fieldValue) -> {
                BsonValue bsonValue = BSONTypeMapper.convertObjectToBSONValue(fieldValue);
                bsonDocument.put(fieldName, bsonValue);
            });
        }
    }

}
