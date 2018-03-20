package com.dena.platform.core.feature.security.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * App User information
 *
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class APPUser {

    public final static String EMAIL_FIELD_NAME = "email";

    public final static String PASSWORD_FIELD_NAME = "password";

    public final static String APPNAME_FIELD_NAME = "password";

    private String email;

    private String password;

    private String appName;

    private Map<String, Object> otherFields = new HashMap<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void addProperty(String name, Object value) {
        if (StringUtils.isNotBlank(name) && value != null) {
            otherFields.put(name, value);
        }
    }
}