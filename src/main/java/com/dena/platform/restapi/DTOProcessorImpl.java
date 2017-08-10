package com.dena.platform.restapi;

import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.entity.BaseEntity;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
@Service("denaDTOProcessor")
public class DTOProcessorImpl implements DTOProcessor {
    private final static Logger log = getLogger(DTOProcessorImpl.class);

    @Override
    public void processDTO(DenaRequestContext denaRequestContext) {
        String requestBody = denaRequestContext.getRequestBody();
        EntityDTO entityDTO = JSONMapper.createObjectFromJSON(requestBody, EntityDTO.class);
        System.out.println(findEntityType(entityDTO));
    }

    private Class<?> findEntityType(EntityDTO entityDTO) {
        String className = entityDTO.getEntityType();

        try {
            Class<?> aClass = Class.forName(className);
            return aClass;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Can not find class", ex);
        }

    }


}
