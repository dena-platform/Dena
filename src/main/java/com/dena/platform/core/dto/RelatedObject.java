package com.dena.platform.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class RelatedObject {
    @JsonProperty(value = "id", required = true)
    private String relatedObjectId;

    @JsonProperty(value = "type", required = true)
    private String typeName;


    public String getRelatedObjectId() {
        return relatedObjectId;
    }

    public void setRelatedObjectId(String relatedObjectId) {
        this.relatedObjectId = relatedObjectId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
