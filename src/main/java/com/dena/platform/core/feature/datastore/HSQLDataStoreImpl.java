package com.dena.platform.core.feature.datastore;

import com.dena.platform.common.persistense.HSQL.HSQLUtils;
import com.dena.platform.core.DenaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Service("denaHSQLDataStoreImpl")
public class HSQLDataStoreImpl implements DenaDataStore {
    private final static Logger log = LoggerFactory.getLogger(HSQLDataStoreImpl.class);

    @Override
    public void storeObject(DenaObject denaObject) {
//        try {
//            HSQLUtils.createTableIfNotExist(denaObject.getTableName());
//            HSQLUtils.storeObjectInTable(denaObject.getTableName(), Integer.valueOf(denaObject.getEntityId()), denaObject.notMappedProperties);
//
//
//        } catch (SQLException | ClassNotFoundException e) {
//            log.error("Error in storing object [{}]", denaObject);
//            throw new DataStoreException(e);
//        }
    }

    @Override
    public DenaObject findObject(Integer objectId) {

        return null;
    }


}
