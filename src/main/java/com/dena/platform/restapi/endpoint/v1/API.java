package com.dena.platform.restapi.endpoint.v1;

import com.dena.platform.restapi.DenaRestProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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


    /**
     * Update object in data store. calling this web service can lead to new field or relation creation
     * @return number of updated objects
     */

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
     * Delete only relation between (parent --> child)
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
     * Find object with id or find related objects
     *
     * @return
     */
    @GetMapping(path = {
            "/{app-id}/{type-name}/{object-id}",
            "/{app-id}/{type-name}/{object-id}/relation/{relation-name}"})
    public ResponseEntity findObject() {
        return denaRestProcessor.handleFindObject();

    }

}
