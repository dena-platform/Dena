package com.dena.platform.rest.dto;

import com.dena.platform.utils.TestUtils;
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
public class TestDenaResponse {

    @JsonProperty("status")
    public Integer httpStatusCode;


    @JsonProperty("timestamp")
    public Long timestamp;

    @JsonProperty("create_object_count(s)")
    public Long createObjectCount;

    @JsonProperty("create_user_count(s)")
    public Integer createUserCount;

    @JsonProperty("create_table_count(s)")
    public Integer createTableCount;

    @JsonProperty("delete_table_count(s)")
    public Integer deleteTableCount;

    @JsonProperty("found_table_count(s)")
    public Integer foundTableCount;

    @JsonProperty("update_object_count(s)")
    public Long updateObjectCount;

    @JsonProperty("delete_object_count(s)")
    public Long deleteObjectCount;

    @JsonProperty("delete_relation_count(s)")
    public Long deleteRelationCount;

    @JsonProperty("found_object_count(s)")
    public Integer foundObjectCount;

    @JsonProperty("objects")
    private List<TestObjectResponse> testObjectResponseList = new ArrayList<>();


    public List<TestObjectResponse> getTestObjectResponseList() {
        return testObjectResponseList;
    }

    public void setTestObjectResponseList(List<TestObjectResponse> testObjectResponseList) {
        this.testObjectResponseList = testObjectResponseList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestDenaResponse that = (TestDenaResponse) o;

        if (createObjectCount != that.createObjectCount) return false;
        if (!TestUtils.isTimeEqualRegardlessOfSecond(Long.valueOf(timestamp), that.timestamp)) {
            return false;
        }
        return testObjectResponseList != null ? testObjectResponseList.equals(that.testObjectResponseList) : that.testObjectResponseList == null;
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + (int) (createObjectCount ^ (createObjectCount >>> 32));
        result = 31 * result + (testObjectResponseList != null ? testObjectResponseList.hashCode() : 0);
        return result;
    }
}
