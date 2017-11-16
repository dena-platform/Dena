package com.dena.platform.common.persistense.MongoDB;

import com.mongodb.MongoClient;
import org.springframework.util.Assert;

import java.sql.SQLException;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public class MongoDBUtils {
    private static MongoClient mongoClient;

    public MongoDBUtils(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }


    public static void createDataBaseIfNotExist(final String databaseName) {
        Assert.notNull(mongoClient, "MongoClient should not be null");
        mongoClient.getDatabase(databaseName);

    }


}
