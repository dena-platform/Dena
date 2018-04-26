package com.dena.platform.restapi.login;

import com.dena.platform.restapi.DenaRestProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Nazarpour.
 */
@RestController

@RequestMapping(value = LoginController.API_PATH, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class LoginController {

    public final static String API_PATH = "/security";

    @Resource(name = "denaRestEntityProcessorImpl")
    protected DenaRestProcessor denaRestProcessor;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = {"/login/{app-id}"}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity login() {
        return denaRestProcessor.login();
    }

    @PostMapping(path = {"/logout/{app-id}"}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity logout() {
        return denaRestProcessor.logout();
    }
}
