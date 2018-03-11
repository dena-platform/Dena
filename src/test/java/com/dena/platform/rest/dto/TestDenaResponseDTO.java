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
public class TestDenaResponseDTO {

    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("count")
    private long count;

    @JsonProperty("total_count")
    private Long totalCount;

    @JsonProperty("page")
    private Long page;

    @JsonProperty("objects")
    private List<TestObjectResponseDTO> testObjectResponseDTOList = new ArrayList<>();


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

    public List<TestObjectResponseDTO> getTestObjectResponseDTOList() {
        return testObjectResponseDTOList;
    }

    public void setTestObjectResponseDTOList(List<TestObjectResponseDTO> testObjectResponseDTOList) {
        this.testObjectResponseDTOList = testObjectResponseDTOList;
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

        TestDenaResponseDTO that = (TestDenaResponseDTO) o;

        if (count != that.count) return false;
        if (!TestUtils.isTimeEqualRegardlessOfSecond(Long.valueOf(timestamp), Long.valueOf(that.getTimestamp()))) {
            return false;
        }
        return testObjectResponseDTOList != null ? testObjectResponseDTOList.equals(that.testObjectResponseDTOList) : that.testObjectResponseDTOList == null;
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + (int) (count ^ (count >>> 32));
        result = 31 * result + (testObjectResponseDTOList != null ? testObjectResponseDTOList.hashCode() : 0);
        return result;
    }
}
