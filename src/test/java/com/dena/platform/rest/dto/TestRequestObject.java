package com.dena.platform.rest.dto;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TestRequestObject {
    private Map<String, Object> fields = new HashMap<>();

    @JsonProperty(value = "object_id")
    private String objectId;

    @JsonProperty("actor_user")
    private String actorUsername;

    @JsonProperty(value = "related_objects")
    private List<TestDenaRelation> relatedObjects = new ArrayList<>();

    public void addRelatedObject(TestDenaRelation testDenaRelation) {
        relatedObjects.add(testDenaRelation);
    }

    public List<TestDenaRelation> getRelatedObjects() {
        return relatedObjects;
    }

    public void setRelatedObjects(List<TestDenaRelation> relatedObjects) {
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

    public String getActorUsername() {
        return actorUsername;
    }

    public void setActorUsername(String actorUsername) {
        this.actorUsername = actorUsername;
    }
}
