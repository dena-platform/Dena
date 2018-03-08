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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Component
public class MongoDBUtils {
    private final static Logger log = LoggerFactory.getLogger(MongoDBUtils.class);
    private static MongoClient mongoClient;

    public static final String ID = "_id";

    @Autowired
    public MongoDBUtils(MongoClient mongoClient) {
        MongoDBUtils.mongoClient = mongoClient;
    }

    public static MongoDatabase getDataBase(final String databaseName) {
        return mongoClient.getDatabase(databaseName);

    }

    public static void createDocument(MongoDatabase mongoDatabase, String collectionName, List<BsonDocument> document) {
        Assert.hasLength(collectionName, "Collection should not be empty or null");
        Assert.notEmpty(document, "Document should not be null");

        mongoDatabase
                .getCollection(collectionName, BsonDocument.class)
                .insertMany(document);

        log.info("Creating document(s) [{}] successfully", document);
    }

    public static void updateDocument(MongoDatabase mongoDatabase, String collectionName, List<? extends Document> documents) {

        ArrayList<WriteModel<Document>> updates = new ArrayList<>();
        documents.forEach(document -> {
            Bson foundDocument = Filters.eq(ID, document.get(ID));
            Document data = new Document("$set", document);
            UpdateOneModel<Document> updateOneModel = new UpdateOneModel<>(foundDocument, data);
            updates.add(updateOneModel);
        });

        BulkWriteResult res = mongoDatabase
                .getCollection(collectionName)
                .bulkWrite(updates, new BulkWriteOptions().ordered(true));

        log.info("Updates: [{}] document(s) count", res.getModifiedCount());
    }

    public static long deleteDocument(MongoDatabase mongoDatabase, String collectionName, List<String> documentIds) {
        List<ObjectId> objectIdList = documentIds.stream().map(ObjectId::new).collect(Collectors.toList());

        DeleteResult deleteResult = mongoDatabase.getCollection(collectionName).deleteMany(Filters.in(ID, objectIdList));
        log.info("Deletes: [{}] document(s) count", deleteResult.getDeletedCount());
        return deleteResult.getDeletedCount();
    }

    public static long deleteRelationWithObjectId(MongoDatabase mongoDatabase, String typeName1, String objectId1, String typeName2, String objectId2) {
        Bson searchDocument = Filters.eq(ID, new ObjectId(objectId1));
        Document update = new Document(typeName2, new ObjectId(objectId2));

        UpdateResult updateResult = mongoDatabase
                .getCollection(typeName1)
                .updateOne(searchDocument, new Document("$pull", update));


        log.info("Updates: [{}] document(s) count", updateResult.getModifiedCount());
        return updateResult.getModifiedCount();
    }

    @SuppressWarnings("unchecked")
    public static long deleteRelationWithType(MongoDatabase mongoDatabase, String typeName1, String objectId, String typeName2) {
        Document searchDocument = new Document(ID, new ObjectId(objectId));
        Document update = new Document(typeName2, "");
        int deleteCount = 0;
        Optional<BsonDocument> document = findDocumentById(mongoDatabase, typeName1, objectId);

        if (document.isPresent()) {
            deleteCount = ((List<ObjectId>) document.get().get(typeName2)).size();
        }

        UpdateResult updateResult = mongoDatabase
                .getCollection(typeName1)
                .updateOne(searchDocument, new Document("$unset", update));


        log.info("Updates: [{}] document(s) count", deleteCount);
        return deleteCount;
    }


    public static Optional<BsonDocument> findDocumentById(MongoDatabase mongoDatabase, String collectionName, String id) {
        BsonDocument bsonDocument = mongoDatabase.getCollection(collectionName, BsonDocument.class)
                .find(Filters.eq(ID, new ObjectId(id)))
                .first();

        return Optional.ofNullable(bsonDocument);
    }

    public static List<Document> findRelatedDocument(MongoDatabase mongoDatabase, BsonDocument parentDocument, String targetType, DenaPager pager) {
        List<Object> otherObjectIds = BsonTypeMapper.convertBsonArrayToJavaArray(parentDocument.get(targetType).asArray());
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

}
