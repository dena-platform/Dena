package com.dena.platform.core.feature.persistence.mongodb;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.feature.persistence.SchemaManager;
import com.dena.platform.core.feature.persistence.exception.DataStoreException;
import com.mongodb.client.MongoDatabase;
import org.springframework.stereotype.Service;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Service("denaMongoDBSchemaManager")
public class SchemaManagerImpl implements SchemaManager {
    @Override
    public int createSchema(String appName, String schemaName) {
        final int createSchemaCount = 1;
        try {
            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            MongoDBUtils.createSchema(mongoDatabase, schemaName);
            return createSchemaCount;
        } catch (DataStoreException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataStoreException("Error in creating schema", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }
}
