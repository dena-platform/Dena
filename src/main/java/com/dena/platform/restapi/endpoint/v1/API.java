package com.dena.platform.restapi.endpoint.v1;

import com.dena.platform.common.web.DenaRequestContext;
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
    protected DenaRestProcessor denaRestProcessor;

    /**
     * Create new object in data store. This webservice may also create relation between objects
     *
     * @return number of created objects
     */
    @PostMapping(path = "/{app-id}/{type-name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createObjects() {
        return denaRestProcessor.handleCreateObject();
    }

    @PutMapping(path = "/{app-id}/{type-name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity updateObjects() {
        return denaRestProcessor.handleUpdateObject();
    }

    /**
     * Delete one or many specified objects
     *
     * @return
     */
    @DeleteMapping(path = "/{app-id}/{type-name}/{object-id}")
    public ResponseEntity deleteObjects() {
        return denaRestProcessor.handleDeleteObject();

    }


    /**
     * Delete only relation between (object --> object) or (object --> target_type)
     *
     * @return
     */
    @DeleteMapping(path = {
            "/{app-id}/{type-name}/{object-id}/relation/{type-name-2}/{object-id-2}",
            "/{app-id}/{type-name}/{object-id}/relation/{type-name-2}"})
    public ResponseEntity deleteRelationWithObjectId() {
        return denaRestProcessor.handleDeleteRelation();
    }

    /**
     * Find object with id or find relation of an object
     *
     * @return
     */
    @GetMapping(path = {
            "/{app-id}/{type-name}/{object-id}",
            "/{app-id}/{type-name}/{object-id}/relation/{target-type}"})
    public ResponseEntity findObject() {
        return denaRestProcessor.handleFindObject();

    }

}
