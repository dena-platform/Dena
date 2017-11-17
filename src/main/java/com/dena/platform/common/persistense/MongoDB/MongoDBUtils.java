package com.dena.platform.common.persistense.MongoDB;

import com.dena.platform.core.DenaObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public class MongoDBUtils {
    private final static Logger log = LoggerFactory.getLogger(MongoDBUtils.class);

    private static MongoClient mongoClient;

    public MongoDBUtils(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public static MongoDatabase createDataBaseIfNotExist(final String databaseName) {
        Assert.notNull(mongoClient, "MongoClient should not be null");
        return mongoClient.getDatabase(databaseName);

    }

    public static void createDocument(MongoDatabase mongoDatabase, String collectionName, List<? extends Document> document) {
        mongoDatabase
                .getCollection(collectionName)
                .insertMany(document);

        log.info("Creating document [{}] successfully", document);
    }


}
