package com.dena.platform.core.feature.datastore;

import com.dena.platform.restapi.EntityDTO;
import org.springframework.stereotype.Service;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
@Service("denaHSQLDataStoreImpl")
public class HSQLDataStoreImpl implements DenaDataStore {
    @Override
    public void storeObject(EntityDTO entityDTO) {

    }

    @Override
    public EntityDTO findObject(long objectId) {
        return null;
    }


}
