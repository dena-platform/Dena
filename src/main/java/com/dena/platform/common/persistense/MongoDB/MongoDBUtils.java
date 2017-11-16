package com.dena.platform.common.persistense.MongoDB;

import com.dena.platform.core.DenaObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.util.Assert;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public class MongoDBUtils {
    private static MongoClient mongoClient;

    public MongoDBUtils(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public static MongoDatabase createDataBaseIfNotExist(final String databaseName) {
        Assert.notNull(mongoClient, "MongoClient should not be null");
        mongoClient.getDatabase(databaseName);

    }

    public static void createDocument(MongoDatabase mongoDatabase, String collectionName, Document document) {
        mongoDatabase
                .getCollection(collectionName)
                .insertOne(document);

    }


}
