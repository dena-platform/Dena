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
    private String email;

    private String password;

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

    public void addProperty(String name, Object value) {
        if (StringUtils.isNotBlank(name) && value != null) {
            otherFields.put(name, value);
        }
    }
}
