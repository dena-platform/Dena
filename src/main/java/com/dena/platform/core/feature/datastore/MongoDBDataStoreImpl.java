package com.dena.platform.core.feature.datastore;

import com.dena.platform.common.persistense.MongoDB.MongoDBUtils;
import com.dena.platform.core.DenaObject;
import com.mongodb.MongoClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
    public void storeObject(DenaObject denaObject) {
        MongoDBUtils.createDataBaseIfNotExist(denaObject.getAppName());
        
    }

    @Override
    public DenaObject findObject(Integer objectId) {
        return null;
    }
}
