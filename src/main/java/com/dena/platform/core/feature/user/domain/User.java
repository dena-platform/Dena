package com.dena.platform.core.feature.user.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * App User information
 *
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {

    public final static String EMAIL_FIELD_NAME = "email";

    public final static String PASSWORD_FIELD_NAME = "password";

    public final static String IS_ACTIVE = "is_active";

    public final static String LAST_VALID_TOKEN = "last_valid_token";

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String unencodedPassword;

    @JsonProperty("is_active")
    private Boolean isActive;

    private String lastValidToken;

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

    public String getUnencodedPassword() {
        return unencodedPassword;
    }

    public void setUnencodedPassword(String unencodedPassword) {
        this.unencodedPassword = unencodedPassword;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getLastValidToken() {
        return lastValidToken;
    }

    public void setLastValidToken(String lastValidToken) {
        this.lastValidToken = lastValidToken;
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

        public UserBuilder withStatus(boolean isActive) {
            user.setActive(isActive);
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
