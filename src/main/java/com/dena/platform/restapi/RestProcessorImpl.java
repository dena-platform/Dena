package com.dena.platform.restapi;

import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.DenaRequestContext;
import com.dena.platform.core.feature.datastore.DenaDataStore;
import com.dena.platform.restapi.dto.DenaResponse;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Service("denaRestEntityProcessorImpl")
public class RestProcessorImpl implements RestEntityProcessor {
    private final static Logger log = getLogger(RestProcessorImpl.class);

    @Resource(name = "denaMongoDBDataStoreImpl")
    private DenaDataStore denaDataStore;


    @Override
    public ResponseEntity processRestRequest(DenaRequestContext denaRequestContext) {
        String requestBody = denaRequestContext.getRequestBody();

        // Creating new object
        if (denaRequestContext.isPostRequest()) {
            List<DenaObject> denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);
            denaDataStore.storeObjects(denaObjects, denaRequestContext.getAppName(), denaRequestContext.getPluralTypeName());

            DenaResponse denaResponse=DenaResponse.DenaResponseBuilder.aDenaResponse()
                    .withObjectResponseList(denaObjects.)

            return ResponseEntity.created(denaObjects).body(denaObjects);
        }

        return ResponseEntity.badRequest().build();

    }

}
