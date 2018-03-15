package com.dena.platform.core.feature.persistence.mongodb;

import com.dena.platform.core.feature.persistence.DenaPager;
import com.mongodb.MongoClient;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
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

    public static void createDocuments(MongoDatabase mongoDatabase, String collectionName, BsonDocument... bsonDocuments) {
        Assert.hasLength(collectionName, "Collection should not be empty");
        Assert.notEmpty(bsonDocuments, "Documents should not be empty");


        List<BsonDocument> bsonDocumentList = Arrays.asList(bsonDocuments);

        mongoDatabase
                .getCollection(collectionName, BsonDocument.class)
                .insertMany(bsonDocumentList);

        log.info("Creating document(s) [{}] successfully", bsonDocumentList);
    }

    public static void updateDocument(MongoDatabase mongoDatabase, String collectionName, BsonDocument... bsonDocuments) {

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

    public static long deleteDocument(MongoDatabase mongoDatabase, String collectionName, String... documentIds) {
        List<ObjectId> objectIdList = new LinkedList<>();

        for (String documentId : documentIds) {
            objectIdList.add(new ObjectId(documentId));
        }

        DeleteResult deleteResult = mongoDatabase.getCollection(collectionName).deleteMany(Filters.in(ID, objectIdList));
        log.info("Deletes: [{}] document(s)", deleteResult.getDeletedCount());
        return deleteResult.getDeletedCount();
    }

    public static long deleteRelationWithObjectId(MongoDatabase mongoDatabase, String typeName1, String objectId1, String typeName2, String objectId2) {
        Bson searchDocument = Filters.eq(ID, new ObjectId(objectId1));
        Document update = new Document(typeName2, new ObjectId(objectId2));

        UpdateResult updateResult = mongoDatabase
                .getCollection(typeName1)
                .updateOne(searchDocument, new Document("$pull", update));


        log.info("Updates: [{}] document(s)", updateResult.getModifiedCount());
        return updateResult.getModifiedCount();
    }

    @SuppressWarnings("unchecked")
    public static long deleteRelationWithType(MongoDatabase mongoDatabase, String typeName1, String objectId, String typeName2) {
        Document searchDocument = new Document(ID, new ObjectId(objectId));
        Document update = new Document(typeName2, "");
        int deleteCount = 0;
        List<BsonDocument> bsonDocuments = findDocumentById(mongoDatabase, typeName1, objectId);

        if (CollectionUtils.isNotEmpty(bsonDocuments)) {
            deleteCount = ((List<ObjectId>) bsonDocuments.get(0).get(typeName2)).size();
        }

        UpdateResult updateResult = mongoDatabase
                .getCollection(typeName1)
                .updateOne(searchDocument, new Document("$unset", update));


        log.info("Updates: [{}] document(s)", deleteCount);
        return deleteCount;
    }

    public static List<BsonDocument> findDocumentById(MongoDatabase mongoDatabase, String collectionName, String... documentIds) {
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

    public static List<Document> findRelatedDocument(MongoDatabase mongoDatabase, BsonDocument parentDocument, String targetType, DenaPager pager) {
        List<Object> otherObjectIds = BSONTypeMapper.convertBSONArrayToJavaArray(parentDocument.get(targetType).asArray());
        Bson searchDocument = Filters.in(ID, otherObjectIds);

        int startIndex = (int) pager.getCount() * pager.getLimit();

        pager.setCount(otherObjectIds.size());
        List<Document> documentList = mongoDatabase.getCollection(targetType)
                .find(searchDocument)
                .skip(startIndex)
                .batchSize(pager.getLimit())
                .limit(pager.getLimit())
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
    public static boolean isCollectionExist(final MongoDatabase mongoDatabase, final String collectionName) {
        List<String> collectionList = mongoDatabase.listCollectionNames().into(new ArrayList<>());
        return collectionList.contains(collectionName);
    }

}
