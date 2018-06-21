package com.dena.platform.core.feature.persistence.mongodb;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.SchemaManager;
import com.dena.platform.core.feature.persistence.exception.DataStoreException;
import com.mongodb.Block;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Service("denaMongoDBSchemaManager")
public class MongoDBSchemaManagerImpl implements SchemaManager {
    private final static Logger log = LoggerFactory.getLogger(MongoDBSchemaManagerImpl.class);

    @Override
    public int createSchema(String appName, String schemaName) {
        final int createSchemaCount = 1;
        try {
            log.info("Creating schema [{}]", schemaName);

            MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
            if (MongoDBUtils.isSchemaExist(mongoDatabase, schemaName)) {
                throw new DataStoreException(String.format("Schema with name [%s] already exist", schemaName),
                        ErrorCode.SCHEMA_ALREADY_EXIST_EXCEPTION);
            }

            MongoDBUtils.createSchema(mongoDatabase, schemaName);

            log.info("Created schema [{}] successfully", schemaName);
            return createSchemaCount;
        } catch (DataStoreException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataStoreException("Error in creating schema", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }
    }

    @Override
    public List<DenaObject> findAllSchema(String appName) {
        List<DenaObject> result = new ArrayList<>();

        MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
        mongoDatabase.listCollectionNames()
                .forEach((Block<? super String>) collectionName -> {
                    DenaObject denaObject = new DenaObject();
                    long recordCount = mongoDatabase.getCollection(collectionName).count();
                    denaObject.addField("name", collectionName);
                    denaObject.addField("record_count(s)", recordCount);

                    result.add(denaObject);
                });

        return result;
    }

    @Override
    public int deleteSchema(String appName, String schemaName) {
        MongoDatabase mongoDatabase = MongoDBUtils.getDataBase(appName);
        log.info("Deleting schema [{}]", schemaName);
        try {
            if (!MongoDBUtils.isSchemaExist(mongoDatabase, schemaName)) {
                log.warn("Schema [{}] not found", schemaName);
                return 0;
            }
            mongoDatabase.getCollection(schemaName).drop();
            log.info("Schema [{}] deleted successfully", schemaName);
            return 1;
        } catch (DataStoreException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataStoreException("Error in creating schema", ErrorCode.GENERAL_DATA_STORE_EXCEPTION, ex);
        }

    }
}
