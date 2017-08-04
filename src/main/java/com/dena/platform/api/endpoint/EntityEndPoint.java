package com.dena.platform.api.endpoint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
@RestController
@RequestMapping(EntityEndPoint.API_PATH)
public class EntityEndPoint {

    public final static String API_PATH = "/v1/";


    @RequestMapping
    public void addEntity(HttpServletRequest request) {
    }

    

}
