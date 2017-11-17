package com.dena.platform.core.feature.datastore;

import com.dena.platform.common.persistense.MongoDB.MongoDBUtils;
import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.DenaObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
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
    public void storeObjects(List<DenaObject> denaObjects, String appName, String typeName) {
        List<Document> documentList = new ArrayList<>();
        MongoDatabase mongoDatabase = MongoDBUtils.createDataBaseIfNotExist(appName);

        denaObjects.forEach(denaObject -> {
            Document document = Document.parse(JSONMapper.createJSONFromObject(denaObject));
            documentList.add(document);
        });

        MongoDBUtils.createDocument(mongoDatabase, typeName, documentList);

    }

    @Override
    public DenaObject findObject(Integer objectId) {
        return null;
    }
}
