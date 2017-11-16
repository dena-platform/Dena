package com.dena.platform.core.feature.datastore;

import com.dena.platform.common.persistense.MongoDB.MongoDBUtils;
import com.dena.platform.core.DenaObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@Service("denaMongoDBDataStoreImpl")
public class MongoDBDataStoreImpl implements DenaDataStore {

    @Resource
    private MongoClient mongoClient;

    @PostConstruct
    public void init() {
        // initialize MongoDBUtils
        MongoDBUtils mongoDBUtils = new MongoDBUtils(mongoClient);

    }

    @Override
    public void storeObjects(List<DenaObject> denaObject) {
        MongoDatabase mongoDatabase = MongoDBUtils.createDataBaseIfNotExist(denaObject.getAppName());
        Document document = new Document();
        document.putAll(denaObject.getObjectsValues());
        document.put()
        MongoDBUtils.createDocument(mongoDatabase, denaObject.getTypeName(), );

    }

    @Override
    public DenaObject findObject(Integer objectId) {
        return null;
    }
}
