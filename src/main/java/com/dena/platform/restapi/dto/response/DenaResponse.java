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
    private Long createObjectCount;

    @JsonProperty("update_object_count(s)")
    private Long updateObjectCount;

    @JsonProperty("delete_object_count(s)")
    private Long deleteObjectCount;

    @JsonProperty("delete_relation_count(s)")
    private Long deleteRelationCount;

    @JsonProperty("found_object_count(s)")
    private Long foundObjectCount;

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

    public void setDeleteRelationCount(Long deleteRelationCount) {
        this.deleteRelationCount = deleteRelationCount;
    }

    public List<DenaObjectResponse> getDenaObjectResponseList() {
        return denaObjectResponseList;
    }

    public void setFoundObjectCount(Long foundObjectCount) {
        this.foundObjectCount = foundObjectCount;
    }

    public static final class DenaResponseBuilder {
        private Long timestamp;
        private Long createObjectCount;
        private Long updateObjectCount;
        private Long deleteObjectCount;
        private Long deleteRelationCount;
        private Long foundObjectCount;
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

        public DenaResponseBuilder withCreateObjectCount(Integer createObjectCount) {
            this.createObjectCount = Long.valueOf(createObjectCount);
            return this;
        }

        public DenaResponseBuilder withUpdateObjectCount(Integer updateObjectCount) {
            this.updateObjectCount = Long.valueOf(updateObjectCount);
            return this;
        }


        public DenaResponseBuilder withDeleteObjectCount(Long deleteObjectCount) {
            this.deleteObjectCount = deleteObjectCount;
            return this;
        }

        public DenaResponseBuilder withDeleteObjectCount(Integer deleteObjectCount) {
            this.deleteObjectCount = Long.valueOf(deleteObjectCount);
            return this;
        }

        public DenaResponseBuilder withDeleteRelationCount(Long deleteRelationCount) {
            this.deleteRelationCount = deleteRelationCount;
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

        public DenaResponseBuilder withFoundObjectCount(int foundObjectCount) {
            this.foundObjectCount = (long) foundObjectCount;
            return this;
        }


        public DenaResponse build() {
            DenaResponse denaResponse = new DenaResponse();
            denaResponse.setTimestamp(timestamp);
            denaResponse.setCreateObjectCount(createObjectCount);
            denaResponse.setUpdateObjectCount(updateObjectCount);
            denaResponse.setDenaObjectResponseList(denaObjectResponseList);
            denaResponse.setDeleteObjectCount(deleteObjectCount);
            denaResponse.setDeleteRelationCount(deleteRelationCount);
            denaResponse.setFoundObjectCount(foundObjectCount);
            return denaResponse;
        }
    }
}
