package com.dena.platform.restapi.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DenaResponse {
    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("create_object_count(s)")
    private Long createObjectCount;

    @JsonProperty("update_object_count(s)")
    private Long updateObjectCount;

    @JsonProperty("delete_object_count(s)")
    private Long deleteObjectCount;

    @JsonProperty("delete_relation_count(s)")
    private Long deleteRelationCount;

    @JsonProperty("page")
    private Long page;

    @JsonProperty("objects")
    private List<DenaObjectResponse> denaObjectResponseList = new ArrayList<>();

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getCreateObjectCount() {
        return createObjectCount;
    }

    public void setCreateObjectCount(Long createObjectCount) {
        this.createObjectCount = createObjectCount;
    }

    public void setDenaObjectResponseList(List<DenaObjectResponse> denaObjectResponseList) {
        this.denaObjectResponseList = denaObjectResponseList;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getUpdateObjectCount() {
        return updateObjectCount;
    }

    public void setUpdateObjectCount(Long updateObjectCount) {
        this.updateObjectCount = updateObjectCount;
    }

    public Long getDeleteObjectCount() {
        return deleteObjectCount;
    }

    public void setDeleteObjectCount(Long deleteObjectCount) {
        this.deleteObjectCount = deleteObjectCount;
    }

    public List<DenaObjectResponse> getDenaObjectResponseList() {
        return denaObjectResponseList;
    }

    public static final class DenaResponseBuilder {
        private Long timestamp;
        private Long createObjectCount;
        private Long updateObjectCount;
        private Long deleteObjectCount;
        private Long deleteRelationCount;

        private Long page;
        private List<DenaObjectResponse> denaObjectResponseList = new ArrayList<>();

        private DenaResponseBuilder() {
        }

        public static DenaResponseBuilder aDenaResponse() {
            return new DenaResponseBuilder();
        }

        public DenaResponseBuilder withTimestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public DenaResponseBuilder withCreateObjectCount(Long count) {
            this.createObjectCount = count;
            return this;
        }

        public DenaResponseBuilder withCreateObjectCount(Integer count) {
            this.createObjectCount = Long.valueOf(count);
            return this;
        }

        public DenaResponseBuilder withUpdateObjectCount(Integer count) {
            this.updateObjectCount = Long.valueOf(count);
            return this;
        }

        public DenaResponseBuilder withUpdateObjectCount(Long count) {
            this.updateObjectCount = count;
            return this;
        }

        public DenaResponseBuilder withDeleteObjectCount(Long count) {
            this.deleteObjectCount = count;
            return this;
        }

        public DenaResponseBuilder withDeleteObjectCount(Integer count) {
            this.deleteObjectCount = Long.valueOf(count);
            return this;
        }

        public DenaResponseBuilder withDeleteRelationCount(Integer count) {
            this.deleteRelationCount = Long.valueOf(count);
            return this;
        }

        public DenaResponseBuilder withDeleteRelationCount(Long count) {
            this.deleteRelationCount = count;
            return this;
        }


        public DenaResponseBuilder withObjectResponseList(List<DenaObjectResponse> denaObjectResponseList) {
            this.denaObjectResponseList = denaObjectResponseList;
            return this;
        }

        public DenaResponseBuilder withPage(long page) {
            this.page = page;
            return this;
        }


        public DenaResponse build() {
            DenaResponse denaResponse = new DenaResponse();
            denaResponse.setTimestamp(timestamp);
            denaResponse.setCreateObjectCount(createObjectCount);
            denaResponse.setUpdateObjectCount(updateObjectCount);
            denaResponse.setDenaObjectResponseList(denaObjectResponseList);
            denaResponse.setDeleteObjectCount(deleteObjectCount);
            denaResponse.setDeleteObjectCount(deleteRelationCount);
            denaResponse.setPage(page);
            return denaResponse;
        }
    }
}
