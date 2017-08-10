package com.dena.platform.restapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class EntityDTO {

    @JsonProperty("entity_id")
    private String entityId;

    @JsonProperty("entity_type")
    private String entityType;


    private Set<String> addationalProperty = new HashSet<>();

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
