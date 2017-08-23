package com.dena.platform.restapi;

import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.entity.DenaEntityMapping;
import com.dena.platform.core.feature.datastore.DenaDataStore;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
@Service("denaRestEntityProcessorImpl")
public class RestEntityProcessorImpl implements RestEntityProcessor {
    private final static Logger log = getLogger(RestEntityProcessorImpl.class);

    @Resource(name = "denaHSQLDataStoreImpl")
    private DenaDataStore denaDataStore;


    @Override
    public void processRestRequest(DenaRequestContext denaRequestContext) {
        String requestBody = denaRequestContext.getRequestBody();
        EntityDTO entityDTO = JSONMapper.createObjectFromJSON(requestBody, EntityDTO.class);
        denaDataStore.storeObject(entityDTO);

    }

    private Class<?> findEntityType(EntityDTO entityDTO) {
        String className = entityDTO.getEntityType();
        return DenaEntityMapping.getKlass(className);
    }


}
