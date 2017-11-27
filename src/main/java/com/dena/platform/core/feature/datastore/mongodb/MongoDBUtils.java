package com.dena.platform.core.feature.datastore.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Component
public class MongoDBUtils {
    private final static Logger log = LoggerFactory.getLogger(MongoDBUtils.class);
    private static MongoClient mongoClient;

    public static final String ID = "_id";
    public static final String APP_NAME = "app_name";
    public static final String TYPE_NAME = "type_name";

    @Autowired
    public MongoDBUtils(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public static MongoDatabase getDataBase(final String databaseName) {
        Assert.notNull(mongoClient, "MongoClient should not be null");
        return mongoClient.getDatabase(databaseName);


    }

    public static void createDocument(MongoDatabase mongoDatabase, String collectionName, List<? extends Document> document) {
        Assert.hasLength(collectionName, "MongoClient should not be null");
        Assert.notEmpty(document, "Document should not be null");

        mongoDatabase
                .getCollection(collectionName)
                .insertMany(document);

        log.info("Creating document(s) [{}] successfully", document);
    }

    public static void updateDocument(MongoDatabase mongoDatabase, String collectionName, List<? extends Document> documents) {
        Assert.hasLength(collectionName, "MongoClient should not be null");
        Assert.notEmpty(documents, "MongoClient should not be null");

        List<String> idCollection = documents
                .stream()
                .map(o -> o.get("_id").toString())
                .collect(Collectors.toList());

        mongoDatabase
                .getCollection(collectionName)
                .up

        log.info("Creating document(s) [{}] successfully", documents);
    }


    public static Document findDocumentById(MongoDatabase mongoDatabase, String collectionName, String id) {
        return mongoDatabase.getCollection(collectionName)
                .find(Filters.eq("_id", new ObjectId(id)))
                .first();
    }

}
