package com.dena.platform.rest.dto;

import com.dena.platform.utils.TestUtils;
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

    @JsonProperty("count")
    private long count;

    @JsonProperty("total_count")
    private Long totalCount;

    @JsonProperty("page")
    private Long page;

    @JsonProperty("objects")
    private List<TestObjectResponse> testObjectResponseList = new ArrayList<>();


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<TestObjectResponse> getTestObjectResponseList() {
        return testObjectResponseList;
    }

    public void setTestObjectResponseList(List<TestObjectResponse> testObjectResponseList) {
        this.testObjectResponseList = testObjectResponseList;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DenaResponse that = (DenaResponse) o;

        if (count != that.count) return false;
        if (!TestUtils.isTimeEqualRegardlessOfSecond(Long.valueOf(timestamp), Long.valueOf(that.getTimestamp()))) {
            return false;
        }
        return testObjectResponseList != null ? testObjectResponseList.equals(that.testObjectResponseList) : that.testObjectResponseList == null;
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + (int) (count ^ (count >>> 32));
        result = 31 * result + (testObjectResponseList != null ? testObjectResponseList.hashCode() : 0);
        return result;
    }
}
