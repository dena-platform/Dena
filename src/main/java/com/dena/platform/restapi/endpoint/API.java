package com.dena.platform.restapi.endpoint;

import com.dena.platform.core.DenaRequestContext;
import com.dena.platform.restapi.RestEntityProcessor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@RestController
@RequestMapping(value = API.API_PATH, produces = {MediaType.APPLICATION_JSON_VALUE})
public class API {

    public final static String API_PATH = "/v1/";

    public static final String APP_ID = "app-id";
    public static final String TYPE_NAME = "type-name";

    @Resource(name = "denaRestEntityProcessorImpl")
    private RestEntityProcessor restEntityProcessor;


    @PostMapping(path = "{app-id}/{type-name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void createObjects(HttpServletRequest request) {
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
