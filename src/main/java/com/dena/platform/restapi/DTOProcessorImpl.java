package com.dena.platform.restapi;

import com.dena.platform.common.web.JSONMapper;
import org.springframework.stereotype.Service;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
@Service("denaDTOProcessor")
public class DTOProcessorImpl implements DTOProcessor {

    @Override
    public void processDTO(DenaRequestContext denaRequestContext) {
        String requestBody = denaRequestContext.getRequestBody();
        EntityDTO entityDTO = JSONMapper.createObjectFromJSON(requestBody, EntityDTO.class);
        
    }

}
