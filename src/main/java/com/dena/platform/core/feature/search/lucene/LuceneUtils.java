package com.dena.platform.core.feature.search.lucene;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.user.domain.User;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author Nazarpour.
 */
public abstract class LuceneUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneUtils.class);

    public static final String APP_ID = "app_id";
    public static final String USER_ID = "user_id";
    public static final String OBJECT_ID = "object_uuid";
    public static final String COLLECTION_NAME = "collection_name";

    public static Document createDocument(String appId, User user, String collectionName, DenaObject denaObject) {
        Map<String, Object> fields = denaObject.getOtherFields();
        Document doc = new Document();
        Field appIdField = new StringField(APP_ID, appId, Field.Store.YES);
        doc.add(appIdField);

        Field userField = new StringField(USER_ID, user.getEmail(), Field.Store.YES);
        doc.add(userField);

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


    public static DenaObject createDenaObject(Document doc) {
        DenaObject dObj = new DenaObject();
        dObj.setObjectId(doc.get(OBJECT_ID));
        dObj.setObjectURI(doc.get(COLLECTION_NAME));
        return dObj;
    }


    public static void commitAllWriters(Map<String, IndexWriter> writers) {
        writers.entrySet().stream().parallel().forEach(entry -> {
            IndexWriter writer = entry.getValue();
            try {
                writer.forceMerge(1);
                writer.commit();
            } catch (IOException e) {
                LOGGER.warn(String.format("unable to merge and commit changes in indexes of app %s", entry.getKey()));
            }
        });
    }

    public static Term createTerm(DenaObject dObject) {
        if (dObject.getObjectId() != null) {
            return new Term(OBJECT_ID, dObject.getObjectId());
        } else {
            return null;
        }
    }
}
