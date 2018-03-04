package com.dena.platform.restapi.dto.response;

import com.dena.platform.core.dto.RelatedObject;
import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DenaObjectResponse {
    private Map<String, Object> fields = new LinkedHashMap<>();

    @JsonProperty(value = "object_id")
    private String objectId;

    @JsonProperty(value = "related_objects")
    private List<RelatedObject> relatedObjects = new ArrayList<>();

    @JsonProperty(value = "object_uri")
    private String objectURI;

    @JsonProperty("update_time")
    private String updateTime;

    @JsonProperty("creation_time")
    private String createTime;


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

    public String getObjectURI() {
        return objectURI;
    }

    public void setObjectURI(String objectURI) {
        this.objectURI = objectURI;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<RelatedObject> getRelatedObjects() {
        return relatedObjects;
    }

    public void setRelatedObjects(List<RelatedObject> relatedObjects) {
        this.relatedObjects = relatedObjects;
    }
}
