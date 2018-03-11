package com.dena.platform.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class RelatedObject {
    @JsonProperty(value = "type", required = true)
    private String type;

    @JsonProperty(value = "relation_name", required = true)
    private String relationName;

    @JsonProperty(value = "target_name", required = true)
    private String targetName;

    @JsonProperty(value = "ids", required = true)
    private List<String> ids;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

}
