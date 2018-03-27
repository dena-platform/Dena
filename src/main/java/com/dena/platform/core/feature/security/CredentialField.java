package com.dena.platform.core.feature.security;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public enum CredentialField {
    IDENTITY("email"),
    PASSWORD("password");

    private String name;

    CredentialField(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
