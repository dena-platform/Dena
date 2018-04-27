package com.dena.platform.core.feature.search.lucene;

import com.dena.platform.core.dto.DenaObject;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;

import java.util.Map;

/**
 * @author Nazarpour.
 */
public abstract class LuceneUtils {
    public static final String APP_ID = "app_id";
    public static final String OBJECT_ID = "object_uuid";
    public static final String COLLECTION_NAME = "collection_name";

    public static Document createDocument(String appId, String collectionName, DenaObject denaObject) {
        Map<String, Object> fields = denaObject.getOtherFields();
        Document doc = new Document();
        Field appIdField = new StringField(APP_ID, appId, Field.Store.YES);
        doc.add(appIdField);


        Field collectionNameField = new StringField(COLLECTION_NAME, collectionName, Field.Store.YES);
        doc.add(collectionNameField);

        Field id = new StringField(OBJECT_ID, denaObject.getObjectId(), Field.Store.YES);
        doc.add(id);

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            Field field = new StringField(entry.getKey(), entry.getValue().toString(), Field.Store.YES);
            doc.add(field);
        }

        return doc;
    }
}
