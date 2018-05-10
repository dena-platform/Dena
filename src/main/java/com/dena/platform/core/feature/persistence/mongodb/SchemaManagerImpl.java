package com.dena.platform.core.feature.persistence.mongodb;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.feature.persistence.SchemaManager;
import com.dena.platform.core.feature.persistence.exception.DataStoreException;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Service("denaMongoDBSchemaManager")
public class SchemaManagerImpl implements SchemaManager {
    private final static Logger log = LoggerFactory.getLogger(SchemaManagerImpl.class);

    @Override
    public int createSchema(String appName, String schemaName) {
        final int createSchemaCount = 1;
        try {
            log.info("Creating schema [{}]", schemaName);


            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            mongoDatabase.createCollection();
            MongoDBUtils.createSchema(mongoDatabase, schemaName);

            log.info("Created schema [{}] successfully", schemaName);
            return createSchemaCount;
        } catch (DataStoreException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataStoreException("Error in creating schema", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }
}
