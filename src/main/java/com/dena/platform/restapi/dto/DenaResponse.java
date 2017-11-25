package com.dena.platform.restapi.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DenaResponse {
    @JsonProperty("timestamp")
    private Date timestamp;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("objects")
    private Map<String, Object> objectFields = new HashMap<>();

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setObjectFields(Map<String, Object> objectFields) {
        this.objectFields = objectFields;
    }

    public static final class DenaResponseBuilder {
        private Date timestamp;
        private Integer count;
        private Map<String, Object> objectFields = new HashMap<>();

        private DenaResponseBuilder() {
        }

        public static DenaResponseBuilder aDenaResponse() {
            return new DenaResponseBuilder();
        }

        public DenaResponseBuilder withTimestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public DenaResponseBuilder withCount(Integer count) {
            this.count = count;
            return this;
        }

        public DenaResponseBuilder withObjectResponseList(Map<String, Object> objectFields) {
            this.objectFields = objectFields;
            return this;
        }

        public DenaResponse build() {
            DenaResponse denaResponse = new DenaResponse();
            denaResponse.setTimestamp(timestamp);
            denaResponse.setCount(count);
            denaResponse.setObjectFields(objectFields);
            return denaResponse;
        }
    }
}
