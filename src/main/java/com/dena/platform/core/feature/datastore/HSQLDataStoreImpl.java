package com.dena.platform.core.feature.datastore;

import com.dena.platform.common.persistense.HSQL.HSQLUtils;
import com.dena.platform.restapi.EntityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
@Service("denaHSQLDataStoreImpl")
public class HSQLDataStoreImpl implements DenaDataStore {
    private final static Logger log = LoggerFactory.getLogger(HSQLDataStoreImpl.class);

    @Override
    public void storeObject(EntityDTO entityDTO) {
        try {
            HSQLUtils.createTableIfNotExist(entityDTO.getTableName());

        } catch (SQLException | ClassNotFoundException e) {
            log.error("Error in storing object [{}]", entityDTO);
            throw new DataStoreException(e);
        }
    }

    @Override
    public EntityDTO findObject(long objectId) {
        return null;
    }


}
