package com.dena.platform.core.feature.datastore.mongodb;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.dto.RelatedObject;
import com.dena.platform.core.feature.datastore.DenaDataStore;
import com.dena.platform.core.feature.datastore.exception.RelationInvalidException;
import com.dena.platform.core.feature.datastore.exception.DataStoreException;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@Service("denaMongoDBDataStoreImpl")
public class MongoDBDataStoreImpl implements DenaDataStore {

    @Override
    public void storeObjects(List<DenaObject> denaObjects, String appName, String typeName) throws DataStoreException {
        List<Document> documentList = new ArrayList<>();

        try {
            MongoDatabase mongoDatabase = MongoDBUtils.createDataBaseIfNotExist(appName);

            denaObjects.forEach(denaObject -> {
                if (!isRelationValid(mongoDatabase, denaObject.getRelatedObjects())) {
                    throw new RelationInvalidException("Relation(s) is invalid");
                }
                Document document = new Document();
                document.put("app_name", appName);
                document.put("type_name", typeName);
                document.putAll(denaObject.getFields());

                // add relation
                if (CollectionUtils.isNotEmpty(denaObject.getRelatedObjects())) {
                    document.putAll(getRelation(denaObject));
                }
                documentList.add(document);
            });

            MongoDBUtils.createDocument(mongoDatabase, typeName, documentList);
        } catch (Exception ex) {
            throw new DataStoreException("Error in storing object", ex);
        }
    }

    @Override
    public DenaObject findObject(Integer objectId) {
        return null;
    }


    private boolean isRelationValid(MongoDatabase mongoDatabase, List<RelatedObject> relatedObjectList) {

        if (CollectionUtils.isEmpty(relatedObjectList)) {
            return true;
        }
        try {
            return relatedObjectList.stream().anyMatch(relatedObject ->
                    MongoDBUtils.findDocumentById(mongoDatabase, relatedObject.getTypeName(), relatedObject.getRelatedObjectId()) != null);
        } catch (Exception ex) {
            // in case of invalid object id
            // nothing
            return false;
        }
    }

    private Map<String, List<ObjectId>> getRelation(DenaObject denaObject) {
        Map<String, List<ObjectId>> references = new HashMap<>();

        denaObject
                .getRelatedObjects()
                .forEach(relatedObject -> {
                    references.putIfAbsent(relatedObject.getTypeName(), new ArrayList<>());
                    ObjectId objectId = new ObjectId(relatedObject.getRelatedObjectId());
                    references.get(relatedObject.getTypeName()).add(objectId);
                });

        return references;
    }
}
