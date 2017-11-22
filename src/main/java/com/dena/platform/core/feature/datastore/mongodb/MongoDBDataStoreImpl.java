package com.dena.platform.core.feature.datastore.mongodb;

import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.DenaObject;
import com.dena.platform.core.feature.datastore.DenaDataStore;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@Service("denaMongoDBDataStoreImpl")
public class MongoDBDataStoreImpl implements DenaDataStore {

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


    private boolean isRelationValid(MongoDatabase mongoDatabase, List<DenaObject> denaObject) {
        List<List<String>> list = denaObject.stream().filter(denaObject1 -> {
            return CollectionUtils.isNotEmpty(denaObject1.getRelatedObjectsId());
        }).map(denaObject1 -> {
            return denaObject1.getRelatedObjectsId();
        }).flatMap(strings -> {
            strings.stream();
        })

        return true;
    }
}
