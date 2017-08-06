package com.dena.platform.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class EntityDTO {

    @JsonProperty("entity_id")
    private String entityId;

    private String entityType;


}
