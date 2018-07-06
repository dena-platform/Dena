package com.dena.platform.core.dto;

import com.dena.platform.common.web.JSONMapper;
import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DenaObject {
    private Map<String, Object> otherFields = new HashMap<>();

    @JsonProperty("object_id")
    private String objectId;

    @JsonProperty("update_time")
    private Long updateTime;

    @JsonProperty("create_time")
    private Long createTime;

    @JsonProperty("object_uri")
    private String objectURI;

    @JsonProperty("actor_user")
    private String actorUsername;

    @JsonProperty("related_objects")
    private List<DenaRelation> denaRelations = new ArrayList<>();

    public List<DenaRelation> getDenaRelations() {
        return Collections.unmodifiableList(denaRelations);
    }

    public void setDenaRelations(List<DenaRelation> denaRelations) {
        this.denaRelations = denaRelations;
    }

    public void addRelatedObjects(DenaRelation denaRelation) {
        this.denaRelations.add(denaRelation);
    }


    @JsonAnySetter
    public void addField(String name, Object value) {
        if (StringUtils.isNotBlank(name) ) {
            otherFields.put(name, value);
        }
    }

    public boolean hasProperty(final String name, final Object value) {
        Object propertyValue = JSONMapper.createHashMapFromObject(this).get(name);
        return Objects.equals(propertyValue, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getOtherFields() {
        return Collections.unmodifiableMap(otherFields);
    }

    public <T> T getField(String name, Class<T> klass) {
        return klass.cast(otherFields.get(name));
    }

    public void addFields(Map<String, Object> fields) {
        otherFields.putAll(fields);
    }

    public void removeField(String fieldName) {
        otherFields.remove(fieldName);
    }

    public void addnew() {
        System.out.println("yes you are ok");
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getObjectURI() {
        return objectURI;
    }

    public void setObjectURI(String objectURI) {
        this.objectURI = objectURI;
    }

    public String getActorUsername() {
        return actorUsername;
    }

    public void setActorUsername(String actorUsername) {
        this.actorUsername = actorUsername;
    }
}


