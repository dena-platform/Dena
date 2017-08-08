package com.dena.platform.restapi.endpoint;

import com.dena.platform.restapi.DTOProcessor;
import com.dena.platform.restapi.DenaRequestContext;
import com.dena.platform.restapi.EntityDTO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
@RestController
@RequestMapping(EntityEndPoint.API_PATH)
public class EntityEndPoint {

    public final static String API_PATH = "/v1/";

    @Resource(name = "denaDTOProcessor")
    private DTOProcessor dtoProcessor;

    @PostMapping
    public void saveEntity(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        dtoProcessor.processDTO(denaRequestContext);

    }


}