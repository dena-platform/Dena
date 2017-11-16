package com.dena.platform.core.feature.datastore;

import com.dena.platform.core.DenaObject;
import org.springframework.stereotype.Service;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@Service("denaMongoDBDataStoreImpl")
public class MongoDBDataStoreImpl implements DenaDataStore {
    @Override
    public void storeObject(DenaObject denaObject) {

    }

    @Override
    public DenaObject findObject(Integer objectId) {
        return null;
    }
}
