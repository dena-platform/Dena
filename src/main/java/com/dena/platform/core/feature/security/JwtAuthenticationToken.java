package com.dena.platform.core.feature.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author Nazarpour.
 */

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String appId;


    public JwtAuthenticationToken(String appId, String userName, String password) {
        super(userName, password);
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }


}