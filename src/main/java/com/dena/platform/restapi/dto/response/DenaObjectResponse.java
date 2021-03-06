package com.dena.platform.restapi.dto.response;

import com.dena.platform.core.dto.DenaRelation;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class DenaObjectResponse {
    private Map<String, Object> fields = new LinkedHashMap<>();

    @JsonProperty(value = "object_id")
    private String objectId;

    @JsonProperty(value = "object_uri")
    private String objectURI;

    @JsonProperty("update_time")
    private Long updateTime;

    @JsonProperty("create_time")
    private Long createTime;

    @JsonAnyGetter
    public Map<String, Object> getFields() {
        return fields;
    }

    @JsonProperty("related_objects")
    private List<DenaRelation> denaRelation;

    @JsonAnySetter
    public void addField(String name, Object value) {
        if (StringUtils.isNoneBlank(name) && value != null) {
            fields.put(name, value);
        }
    }


    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    public List<DenaRelation> getDenaRelation() {
        return denaRelation;
    }

    public void setRelation(List<DenaRelation> denaRelationWrapper) {
        this.denaRelation = denaRelationWrapper;
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

}
