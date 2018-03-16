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
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@Service("denaMongoDBDataStoreImpl")
public class MongoDBDataStoreImpl implements DenaDataStore {
    private final static Logger log = LoggerFactory.getLogger(MongoDBDataStoreImpl.class);


    @Override
    public List<DenaObject> storeObjects(String appName, String typeName, DenaObject... denaObjects) {
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
                bsonDocument.put(MongoDBUtils.OBJECT_URI_FIELD, new BsonString(DenaObjectUtils.getURIForResource(typeName, objectId.toString())));
                addFieldsToBsonDocument(bsonDocument, denaObject.getOtherFields());

                // add relation
                if (CollectionUtils.isNotEmpty(denaObject.getDenaRelations())) {
                    checkRelationValidity(mongoDatabase, denaObject, denaObject.getDenaRelations());
                    bsonDocument.putAll(getRelation(denaObject));
                }

                bsonDocuments.add(bsonDocument);
                ids.add(objectId.toString());
            }

            MongoDBUtils.createDocuments(mongoDatabase, typeName, bsonDocuments.toArray(new BsonDocument[bsonDocuments.size()]));

            return new ArrayList<>(findObject(appName, typeName, ids.toArray(new String[0])));
        } catch (DataStoreException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataStoreException("Error in storing object(s)", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }

    @Override
    public List<DenaObject> updateObjects(final String appName, final String typeName, DenaObject... denaObjects) {
        MongoDatabase mongoDatabase;
        List<BsonDocument> bsonDocumentList = new ArrayList<>();

        if (ArrayUtils.isEmpty(denaObjects)) {
            log.debug("Object request list is empty");
            return Collections.emptyList();
        }

        try {
            mongoDatabase = MongoDBUtils.getDataBase(appName);

            for (DenaObject denaObject : denaObjects) {
                checkIfObjectIdIsExist(mongoDatabase, typeName, denaObject.getObjectId());
                checkRelationValidity(mongoDatabase, denaObject, denaObject.getDenaRelations());
            }

            List<String> ids = new LinkedList<>();

            for (DenaObject denaObject : denaObjects) {
                ObjectId objectId = new ObjectId(denaObject.getObjectId());
                BsonDocument bsonDocument = new BsonDocument();
                bsonDocument.put(MongoDBUtils.ID, new BsonObjectId(objectId));
                bsonDocument.put(MongoDBUtils.UPDATE_TIME_FIELD, new BsonDateTime(DenaObjectUtils.timeStamp()));


                addFieldsToBsonDocument(bsonDocument, denaObject.getOtherFields());

                // update relation
                if (CollectionUtils.isNotEmpty(denaObject.getDenaRelations())) {
                    DenaObject existingDenaObject = findObject(appName, typeName, objectId.toString()).get(0);
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


            MongoDBUtils.updateDocument(mongoDatabase, typeName, bsonDocumentList.toArray(new BsonDocument[0]));

            return new ArrayList<>(findObject(appName, typeName, ids.toArray(new String[0])));
        } catch (DataStoreException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataStoreException("Error in updating object(s)", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }

    }

    @Override
    public long deleteObjects(String appName, String typeName, String... objectIds) {
        if (ArrayUtils.isEmpty(objectIds)) {
            return 0;
        }

        checkObjectIdValidity(objectIds);
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteDocument(mongoDatabase, typeName, objectIds);
        } catch (Exception ex) {
            throw new DataStoreException("Error in deleting object(s)", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }

    }

    @Override
    public long deleteRelation(String appName, String parentTypeName, String parentObjectId, String relationName, String childObjectId) {
        checkObjectIdValidity(parentObjectId, childObjectId);
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteRelationWithObjectId(mongoDatabase, parentTypeName, parentObjectId, relationName, childObjectId);
        } catch (Exception ex) {
            throw new DataStoreException("Error in deleting relation", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }

    @Override
    public long deleteRelation(String appName, String parentTypeName, String parentObjectId, String relationName) {
        checkObjectIdValidity(parentObjectId);

        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            return MongoDBUtils.deleteRelationWithType(mongoDatabase, parentTypeName, parentObjectId, relationName);
        } catch (Exception ex) {
            throw new DataStoreException("Error in deleting relation", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }

    @Override
    public List<DenaObject> findObject(String appName, String typeName, String... objectId) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            List<DenaObject> foundDenaObjects = new ArrayList<>();


            List<BsonDocument> bsonDocuments = MongoDBUtils.findDocumentById(mongoDatabase, typeName, objectId);
            if (CollectionUtils.isEmpty(bsonDocuments)) {
                return Collections.emptyList();
            }

            for (BsonDocument bsonDocument : bsonDocuments) {
                DenaObject denaObject = new DenaObject();
                for (Map.Entry<String, BsonValue> entry : bsonDocument.entrySet()) {
                    String fieldName = entry.getKey();
                    BsonValue fieldValue = entry.getValue();

                    if (fieldValue.isDocument() && fieldValue.asDocument().containsKey(MongoDBUtils.RELATION_TYPE)) {
                        // this field is relation
                        BsonDocument relatedDocument = fieldValue.asDocument();

                        BsonArray idArray = relatedDocument.getArray(MongoDBUtils.RELATION_IDS);
                        List<String> idStringArray = BSONTypeMapper.convertBSONArrayToJavaArray(idArray)
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
                        ArrayList<Object> listOfObject = BSONTypeMapper.convertBSONArrayToJavaArray(values);
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

                foundDenaObjects.add(denaObject);
            }

            return foundDenaObjects;
        } catch (Exception ex) {
            throw new DataStoreException("Error in finding object", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }


    @Override
    public List<DenaObject> findObjectRelation(String appName, String parentType, String objectId, String targetType, DenaPager denaPager) {
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            List<BsonDocument> parentDocument = MongoDBUtils.findDocumentById(mongoDatabase, parentType, objectId);

            if (CollectionUtils.isEmpty(parentDocument)) {
                return null;
            }

            List<DenaObject> denaObjects = new ArrayList<>();
            List<Document> relatedDocuments = MongoDBUtils.findRelatedDocument(mongoDatabase, parentDocument.get(0), targetType, denaPager);

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


    private void checkRelationValidity(MongoDatabase mongoDatabase, DenaObject parentObject, List<DenaRelation> relationList) {

        if (CollectionUtils.isNotEmpty(relationList)) {
            log.debug("Check validity for relation(s) [{}]", relationList);


            if (StringUtils.isNotEmpty(parentObject.getObjectId())) {
                // This is an update operation
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
                            boolean isCollectionExist = MongoDBUtils.isCollectionExist(mongoDatabase, denaRelation.getTargetName());
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

    private void checkIfObjectIdIsExist(MongoDatabase mongoDatabase, String typeName, String objectId) {
        checkObjectIdValidity(objectId);

        boolean isObjectIdExist = CollectionUtils.isNotEmpty(MongoDBUtils.findDocumentById(mongoDatabase, typeName, objectId));

        if (!isObjectIdExist) {
            throw new DataStoreException("ObjectId not found exception", ErrorCode.ObjectId_NOT_FOUND_EXCEPTION);
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
                throw new DataStoreException(String.format("ObjectId [%s] invalid exception", objectIds), ErrorCode.ObjectId_INVALID_EXCEPTION);
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
                BsonValue bsonValue = BSONTypeMapper.convertObjectToBSONValue(fieldValue);
                bsonDocument.put(fieldName, bsonValue);
            });
        }
    }

}
