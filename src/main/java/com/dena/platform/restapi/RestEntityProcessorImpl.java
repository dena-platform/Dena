package com.dena.platform.restapi;

import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.entity.DenaEntityMapping;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
@Service("denaRestEntityProcessorImpl")
public class RestEntityProcessorImpl implements RestEntityProcessor {
    private final static Logger log = getLogger(RestEntityProcessorImpl.class);


    @Override
    public void processRestRequest(DenaRequestContext denaRequestContext) {
        String requestBody = denaRequestContext.getRequestBody();
        EntityDTO entityDTO = JSONMapper.createObjectFromJSON(requestBody, EntityDTO.class);

    }

    private Class<?> findEntityType(EntityDTO entityDTO) {
        String className = entityDTO.getEntityType();
        return DenaEntityMapping.getKlass(className);
    }


}
