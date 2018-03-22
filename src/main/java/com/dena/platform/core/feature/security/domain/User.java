package com.dena.platform.core.feature.security.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * App User information
 *
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class User {

    public final static String EMAIL_FIELD_NAME = "email";

    public final static String PASSWORD_FIELD_NAME = "password";

    private String email;

    private String password;

    private String unencodedPassword;

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

    public String getUnencodedPassword() {
        return unencodedPassword;
    }

    public void setUnencodedPassword(String unencodedPassword) {
        this.unencodedPassword = unencodedPassword;
    }

    public void addProperty(final String name, final Object value) {
        if (StringUtils.isNotBlank(name) && value != null) {
            otherFields.put(name, value);
        }
    }

    public void setOtherFields(Map<String, Object> otherFields) {
        this.otherFields = otherFields;
    }

    public Map<String, Object> getOtherFields() {
        return Collections.unmodifiableMap(otherFields);
    }


    public static final class UserBuilder {
        private User user;

        private UserBuilder() {
            user = new User();
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder withEmail(String email) {
            user.setEmail(email);
            return this;
        }

        public UserBuilder withUnencodedPassword(String unencodedPassword) {
            user.setUnencodedPassword(unencodedPassword);
            return this;
        }

        public UserBuilder withPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public UserBuilder withOtherFields(Map<String, Object> otherFields) {
            user.setOtherFields(otherFields);
            return this;
        }

        public User build() {
            return user;
        }
    }
}
