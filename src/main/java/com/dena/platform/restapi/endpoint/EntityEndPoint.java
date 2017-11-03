package com.dena.platform.restapi.endpoint;

import com.dena.platform.restapi.DenaRequestContext;
import com.dena.platform.restapi.RestEntityProcessor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@RestController
@RequestMapping(EntityEndPoint.API_PATH)
public class EntityEndPoint {

    public final static String API_PATH = "/v1/";

    @Resource(name = "denaRestEntityProcessorImpl")
    private RestEntityProcessor restEntityProcessor;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void saveEntity(HttpServletRequest request) {

        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        restEntityProcessor.processRestRequest(denaRequestContext);

    }

    @GetMapping
    public String findEntity(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        restEntityProcessor.processRestRequest(denaRequestContext);

        return null;
    }
}
