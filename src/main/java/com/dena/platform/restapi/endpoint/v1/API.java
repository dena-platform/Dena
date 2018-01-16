package com.dena.platform.restapi.endpoint.v1;

import com.dena.platform.core.DenaRequestContext;
import com.dena.platform.restapi.DenaRestProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@RestController
@RequestMapping(value = API.API_PATH, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class API {

    public final static String API_PATH = "/v1/";

    @Resource(name = "denaRestEntityProcessorImpl")
    private DenaRestProcessor denaRestProcessor;


    @PostMapping(path = "/{app-id}/{type-name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createObjects(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        return denaRestProcessor.handleCreateObject(denaRequestContext);
    }

    @PutMapping(path = "/{app-id}/{type-name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity updateObjects(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        return denaRestProcessor.handleUpdateObject(denaRequestContext);
    }

    /**
     * Delete one or many specified objects
     *
     * @return
     */
    @DeleteMapping(path = "/{app-id}/{type-name}/{object-id}")
    public ResponseEntity deleteObjects(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        return denaRestProcessor.handleDeleteObject(denaRequestContext);

    }


    /**
     * Delete only relation between (object --> object) or (object --> target_type)
     *
     * @return
     */
    @DeleteMapping(path = {
            "/{app-id}/{type-name}/{object-id}/relation/{type-name-2}/{object-id-2}",
            "/{app-id}/{type-name}/{object-id}/relation/{type-name-2}"})
    public ResponseEntity deleteRelationWithObjectId(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        return denaRestProcessor.handleDeleteRelation(denaRequestContext);
    }

    @GetMapping(path = {
            "/{app-id}/{type-name}/{object-id}",
            "/{app-id}/{type-name}/{object-id}/relation/{target-type}"})
    public ResponseEntity findObject(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        return denaRestProcessor.handleFindObject(denaRequestContext);

    }

}
