package com.dena.platform.core.feature.user.domain;

import com.dena.platform.core.dto.DenaObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * App User information
 *
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User extends DenaObject {

    public final static String EMAIL_FIELD_NAME = "email";

    public final static String PASSWORD_FIELD_NAME = "password";

    public final static String IS_ACTIVE_FIELD_NAME = "is_active";

    public final static String TOKEN_FIELD_NAME = "token";


    @JsonProperty("password")
    private String unencodedPassword;

    public String getEmail() {
        return getField(EMAIL_FIELD_NAME, String.class);
    }

    public void setEmail(String email) {
        addField(EMAIL_FIELD_NAME, email);
    }

    public String getPassword() {
        return getField(PASSWORD_FIELD_NAME, String.class);
    }

    public void setPassword(String password) {
        addField(PASSWORD_FIELD_NAME, password);
    }

    public String getUnencodedPassword() {
        return unencodedPassword;
    }

    public void setUnencodedPassword(String unencodedPassword) {
        this.unencodedPassword = unencodedPassword;
    }

    public Boolean getActive() {
        return getField(IS_ACTIVE_FIELD_NAME, Boolean.class);
    }

    public void setActive(Boolean isActive) {
        addField(IS_ACTIVE_FIELD_NAME, isActive);
    }

    public String getToken() {
        return getField(TOKEN_FIELD_NAME, String.class);
    }

    public void setToken(String token) {
        addField(TOKEN_FIELD_NAME, token);
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

        public UserBuilder withStatus(boolean isActive) {
            user.setActive(isActive);
            return this;
        }

        public UserBuilder withOtherFields(Map<String, Object> otherFields) {
            user.addFields(otherFields);
            return this;
        }

        public User build() {
            return user;
        }
    }
}
