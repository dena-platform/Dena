package com.dena.platform.rest.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@JsonAutoDetect(fieldVisibility = NONE, getterVisibility = NONE, setterVisibility = NONE)
public class TestObjectResponseDTO {
    private Map<String, Object> fields = new LinkedHashMap<>();

    @JsonProperty("object_id")
    public String objectId;

    @JsonProperty(value = "object_uri")
    public String objectURI;

    @JsonProperty("update_time")
    public Long updateTime;

    @JsonProperty("create_time")
    public Long createTime;

    @JsonAnyGetter
    public Map<String, Object> getAllFields() {
        return fields;
    }

    public <T> T getField(String name, Class<T> klass) {
        return klass.cast(fields.get(name));
    }


    @JsonAnySetter
    public void addProperty(String name, Object value) {
        if (StringUtils.isNoneBlank(name) && value != null) {
            fields.put(name, value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestObjectResponseDTO that = (TestObjectResponseDTO) o;

        if (fields != null ? !fields.equals(that.fields) : that.fields != null) return false;
        return objectId.equals(that.objectId);
    }

    @Override
    public int hashCode() {
        int result = fields != null ? fields.hashCode() : 0;
        result = 31 * result + objectId.hashCode();
        return result;
    }
}
