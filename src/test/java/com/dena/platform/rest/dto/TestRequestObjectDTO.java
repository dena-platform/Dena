package com.dena.platform.rest.dto;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TestRequestObjectDTO {
    private Map<String, Object> fields = new HashMap<>();

    @JsonProperty(value = "object_id")
    private String objectId;

    @JsonProperty(value = "related_objects")
    private List<TestRelatedObjectDTO> relatedObjects = new ArrayList<>();

    public List<TestRelatedObjectDTO> getRelatedObjects() {
        return relatedObjects;
    }

    public void setRelatedObjects(List<TestRelatedObjectDTO> relatedObjects) {
        this.relatedObjects = relatedObjects;
    }


    @JsonAnySetter
    public void addProperty(String name, Object value) {
        if (StringUtils.isNoneBlank(name) && value != null) {
            fields.put(name, value);
        }
    }

    @JsonAnyGetter
    public Map<String, Object> getAllFields() {
        return fields;
    }


    public Map<String, Object> getFields() {
        return fields;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}
