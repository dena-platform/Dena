package com.dena.platform.core.feature.persistence.mongodb;

import com.dena.platform.core.feature.persistence.DenaPager;
import com.mongodb.MongoClient;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.BsonDocument;
import org.bson.BsonNull;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Component
public class MongoDBUtils {
    private final static Logger log = LoggerFactory.getLogger(MongoDBUtils.class);
    private static MongoClient mongoClient;

    public static final String ID = "_id";

    // general object field name
    public static final String UPDATE_TIME_FIELD = "update_time";
    public static final String CREATE_TIME_FIELD = "create_time";
    public static final String OBJECT_URI_FIELD = "object_uri";


    // related objects field name
    public static final String RELATION_TARGET_NAME = "target_name";
    public static final String RELATION_IDS = "ids";
    public static final String RELATION_TYPE = "relation_type";


    @Autowired
    public MongoDBUtils(MongoClient mongoClient) {
        MongoDBUtils.mongoClient = mongoClient;
    }

    public static MongoDatabase getDataBase(final String databaseName) {
        return mongoClient.getDatabase(databaseName);

    }


    public static void createSchema(MongoDatabase mongoDatabase, String schemaName) {
        Assert.hasLength(schemaName, "Schema name should not be empty");
        mongoDatabase.createCollection(schemaName);
    }


    /////////////////////////////////////////////
    //           CRUD API
    /////////////////////////////////////////////
    public static void createDocuments(MongoDatabase mongoDatabase, String collectionName, BsonDocument... bsonDocuments) {
        Assert.hasLength(collectionName, "Collection should not be empty");
        Assert.notEmpty(bsonDocuments, "Documents should not be empty");


        List<BsonDocument> bsonDocumentList = Arrays.asList(bsonDocuments);

        mongoDatabase
                .getCollection(collectionName, BsonDocument.class)
                .insertMany(bsonDocumentList);

        log.info("Creating document(s) [{}] successfully", bsonDocumentList);
    }

    public static void mergeUpdateDocument(MongoDatabase mongoDatabase, String collectionName, BsonDocument... bsonDocuments) {

        ArrayList<WriteModel<Document>> updates = new ArrayList<>();
        for (BsonDocument bsonDocument : bsonDocuments) {
            Bson foundDocument = Filters.eq(ID, bsonDocument.get(ID));
            Document data = new Document("$set", bsonDocument);
            UpdateOneModel<Document> updateOneModel = new UpdateOneModel<>(foundDocument, data);
            updates.add(updateOneModel);
        }

        BulkWriteResult res = mongoDatabase
                .getCollection(collectionName)
                .bulkWrite(updates, new BulkWriteOptions().ordered(true));

        log.info("Updates: [{}] document(s)", res.getModifiedCount());
    }

    public static void replaceUpdateDocument(MongoDatabase mongoDatabase, String collectionName, BsonDocument... bsonDocuments) {

        ArrayList<WriteModel<Document>> updates = new ArrayList<>();
        for (BsonDocument bsonDocument : bsonDocuments) {
            Bson filter = Filters.eq(ID, bsonDocument.get(ID));
            ReplaceOneModel<Document> updateOneModel = new ReplaceOneModel<>(filter, convertBsonDocumentToDocument(bsonDocument));
            updates.add(updateOneModel);
        }

        BulkWriteResult res = mongoDatabase
                .getCollection(collectionName)
                .bulkWrite(updates, new BulkWriteOptions().ordered(true));

        log.info("Updates: [{}] document(s)", res.getModifiedCount());
    }


    public static long deleteDocument(MongoDatabase mongoDatabase, String typeName, String... documentIds) {
        List<ObjectId> objectIdList = new LinkedList<>();

        for (String documentId : documentIds) {
            objectIdList.add(new ObjectId(documentId));
        }

        DeleteResult deleteResult = mongoDatabase.getCollection(typeName).deleteMany(Filters.in(ID, objectIdList));
        log.info("Deletes: [{}] document(s)", deleteResult.getDeletedCount());
        return deleteResult.getDeletedCount();
    }

    public static long deleteRelationWithObjectId(MongoDatabase mongoDatabase, String parentType, String parentObjectId,
                                                  String relationName, String childObjectId) {
        Bson searchDocument = Filters.eq(ID, new ObjectId(parentObjectId));
        Bson deleteObjectIdCommand = Updates.pull(relationName + "." + RELATION_IDS, new BsonObjectId(new ObjectId(childObjectId)));


        UpdateResult updateResult = mongoDatabase
                .getCollection(parentType)
                .updateOne(searchDocument, deleteObjectIdCommand);

        log.info("Updates: [{}] document(s)", updateResult.getModifiedCount());
        return updateResult.getModifiedCount();
    }

    public static long deleteRelationWithType(MongoDatabase mongoDatabase, String parentTypeName, String parentObjectId,
                                              String relationName) {
        BsonDocument searchDocument = new BsonDocument(ID, new BsonObjectId(new ObjectId(parentObjectId)));
        BsonDocument update = new BsonDocument(relationName, new BsonNull());
        int deleteCount = 0;
        List<BsonDocument> bsonDocuments = findDocumentById(mongoDatabase, parentTypeName, parentObjectId);

        if (CollectionUtils.isNotEmpty(bsonDocuments) && bsonDocuments.get(0).containsKey(relationName)) {
            deleteCount = bsonDocuments.get(0).getDocument(relationName).getArray(RELATION_IDS).size();
            UpdateResult updateResult = mongoDatabase
                    .getCollection(parentTypeName)
                    .updateOne(searchDocument, new Document("$unset", update));

            log.info("Delete: [{}] relation(s)", deleteCount);
        }

        return deleteCount;
    }


    /////////////////////////////////////////////
    //           SEARCH API
    /////////////////////////////////////////////

    public static List<BsonDocument> findDocumentById(MongoDatabase mongoDatabase, String collectionName,
                                                      String... documentIds) {
        List<ObjectId> objectIds = new ArrayList<>();
        List<BsonDocument> returnList = new LinkedList<>();

        for (String id : documentIds) {
            objectIds.add(new ObjectId(id));
        }

        mongoDatabase.getCollection(collectionName, BsonDocument.class)
                .find(Filters.in(ID, objectIds))
                .into(returnList);


        return returnList;
    }

    public static List<BsonDocument> findALLDocument(MongoDatabase mongoDatabase, String collectionName, DenaPager pager) {
        List<BsonDocument> returnList = new LinkedList<>();
        int startIndex = pager.getStartIndex();

        mongoDatabase.getCollection(collectionName, BsonDocument.class)
                .find()
                .skip(startIndex)
                .batchSize(pager.getPageSize())
                .limit(pager.getPageSize())
                .into(returnList);

        return returnList;
    }


    public static List<BsonDocument> findRelatedDocument(MongoDatabase mongoDatabase, BsonDocument parentDocument,
                                                         String relationName, DenaPager pager) {

        if (!parentDocument.containsKey(relationName)) {
            return Collections.emptyList();
        }

        List<Object> relatedObjectIds = BSONTypeMapper.convertBSONArrayToJava(
                parentDocument.getDocument(relationName).getArray(RELATION_IDS)
        );
        String targetCollectionName = parentDocument.getDocument(relationName).getString(RELATION_TARGET_NAME).getValue();

        Bson searchDocument = Filters.in(ID, relatedObjectIds);

        int startIndex = pager.getStartIndex();

        List<BsonDocument> documentList = mongoDatabase.getCollection(targetCollectionName, BsonDocument.class)
                .find(searchDocument)
                .skip(startIndex)
                .batchSize(pager.getPageSize())
                .limit(pager.getPageSize())
                .into(new ArrayList<>());

        return documentList;

    }

    /**
     * Return true if collection name exist in database
     *
     * @param mongoDatabase  Instance of Mongo database
     * @param collectionName Name of collection that we wand search for
     * @return
     */
    public static boolean isSchemaExist(final MongoDatabase mongoDatabase, final String collectionName) {
        List<String> collectionList = mongoDatabase.listCollectionNames().into(new ArrayList<>());
        return collectionList.contains(collectionName);
    }


    private static Document convertBsonDocumentToDocument(BsonDocument bsonDocument) {
        return Document.parse(bsonDocument.toJson());
    }

}
