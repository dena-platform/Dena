package com.dena.platform.restapi.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DenaResponse {
    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("create_object_count(s)")
    private Integer createObjectCount;

    @JsonProperty("create_table_count(s)")
    private Integer createTableCount;

    @JsonProperty("update_object_count(s)")
    private Integer updateObjectCount;

    @JsonProperty("delete_object_count(s)")
    private Long deleteObjectCount;

    @JsonProperty("delete_relation_count(s)")
    private Long deleteRelationCount;

    @JsonProperty("found_object_count(s)")
    private Integer foundObjectCount;

    @JsonProperty("objects")
    private List<DenaObjectResponse> denaObjectResponseList = new ArrayList<>();

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getCreateObjectCount() {
        return createObjectCount;
    }

    public void setCreateObjectCount(Integer createObjectCount) {
        this.createObjectCount = createObjectCount;
    }

    public Integer getCreateTableCount() {
        return createTableCount;
    }

    public void setCreateTableCount(Integer createTableCount) {
        this.createTableCount = createTableCount;
    }

    public void setDenaObjectResponseList(List<DenaObjectResponse> denaObjectResponseList) {
        this.denaObjectResponseList = denaObjectResponseList;
    }

    public Integer getUpdateObjectCount() {
        return updateObjectCount;
    }

    public void setUpdateObjectCount(Integer updateObjectCount) {
        this.updateObjectCount = updateObjectCount;
    }

    public Long getDeleteObjectCount() {
        return deleteObjectCount;
    }

    public void setDeleteObjectCount(Long deleteObjectCount) {
        this.deleteObjectCount = deleteObjectCount;
    }

    public void setDeleteRelationCount(Long deleteRelationCount) {
        this.deleteRelationCount = deleteRelationCount;
    }

    public List<DenaObjectResponse> getDenaObjectResponseList() {
        return denaObjectResponseList;
    }

    public void setFoundObjectCount(Integer foundObjectCount) {
        this.foundObjectCount = foundObjectCount;
    }


    public static final class DenaResponseBuilder {
        private DenaResponse denaResponse;

        private DenaResponseBuilder() {
            denaResponse = new DenaResponse();
        }

        public static DenaResponseBuilder aDenaResponse() {
            return new DenaResponseBuilder();
        }

        public DenaResponseBuilder withTimestamp(Long timestamp) {
            denaResponse.setTimestamp(timestamp);
            return this;
        }

        public DenaResponseBuilder withCreateObjectCount(int createObjectCount) {
            denaResponse.setCreateObjectCount(createObjectCount);
            return this;
        }

        public DenaResponseBuilder withCreateTableCount(int createSchemaCount) {
            denaResponse.setCreateTableCount(createSchemaCount);
            return this;
        }

        public DenaResponseBuilder withUpdateObjectCount(int updateObjectCount) {
            denaResponse.setUpdateObjectCount(updateObjectCount);
            return this;
        }

        public DenaResponseBuilder withDeleteObjectCount(Long deleteObjectCount) {
            denaResponse.setDeleteObjectCount(deleteObjectCount);
            return this;
        }

        public DenaResponseBuilder withDeleteRelationCount(Long deleteRelationCount) {
            denaResponse.setDeleteRelationCount(deleteRelationCount);
            return this;
        }

        public DenaResponseBuilder withFoundObjectCount(int foundObjectCount) {
            denaResponse.setFoundObjectCount(foundObjectCount);
            return this;
        }

        public DenaResponseBuilder withDenaObjectResponseList(List<DenaObjectResponse> denaObjectResponseList) {
            denaResponse.setDenaObjectResponseList(denaObjectResponseList);
            return this;
        }

        public DenaResponse build() {
            return denaResponse;
        }
    }
}
