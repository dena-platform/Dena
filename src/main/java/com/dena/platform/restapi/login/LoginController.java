package com.dena.platform.restapi.login;

import com.dena.platform.restapi.DenaRestProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Nazarpour.
 */
@RestController
@RequestMapping(value = LoginController.API_PATH, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class LoginController {

    public final static String API_PATH = "/login/";

    @Resource(name = "denaRestEntityProcessorImpl")
    protected DenaRestProcessor denaRestProcessor;


    /////////////////////////////////////////////
    //            login api
    /////////////////////////////////////////////

    @PostMapping(path = {"/{app-id}"})
    public ResponseEntity registerUser() {
        return denaRestProcessor.login();
    }
}
