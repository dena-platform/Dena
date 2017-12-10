package com.dena.platform.restapi.dto.response;

import com.dena.platform.core.dto.RelatedObject;
import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ObjectResponse {
    private Map<String, Object> fields = new LinkedHashMap<>();

    @JsonProperty(value = "related_objects")
    private List<RelatedObject> relatedObjects = new ArrayList<>();

    @JsonProperty(value = "object_id")
    private String objectId;

    @JsonProperty(value = "URI")
    private String URI;

    @JsonAnyGetter
    public Map<String, Object> getAllFields() {
        return fields;
    }


    @JsonAnySetter
    public void addProperty(String name, Object value) {
        if (StringUtils.isNoneBlank(name) && value != null) {
            fields.put(name, value);
        }
    }


    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<RelatedObject> getRelatedObjects() {
        return relatedObjects;
    }

    public void setRelatedObjects(List<RelatedObject> relatedObjects) {
        this.relatedObjects = relatedObjects;
    }
}
