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
    public Long timestamp;

    @JsonProperty("create_object_count(s)")
    public long createObjectCount;

    @JsonProperty("update_object_count(s)")
    public Long updateObjectCount;

    @JsonProperty("delete_object_count(s)")
    public Long deleteObjectCount;

    @JsonProperty("delete_relation_count(s)")
    public Long deleteRelationCount;

    @JsonProperty("found_object_count(s)")
    public Long foundObjectCount;



    @JsonProperty("page")
    public Long page;

    @JsonProperty("objects")
    private List<TestObjectResponseDTO> testObjectResponseDTOList = new ArrayList<>();


    public List<TestObjectResponseDTO> getTestObjectResponseDTOList() {
        return testObjectResponseDTOList;
    }

    public void setTestObjectResponseDTOList(List<TestObjectResponseDTO> testObjectResponseDTOList) {
        this.testObjectResponseDTOList = testObjectResponseDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestDenaResponseDTO that = (TestDenaResponseDTO) o;

        if (createObjectCount != that.createObjectCount) return false;
        if (!TestUtils.isTimeEqualRegardlessOfSecond(Long.valueOf(timestamp), that.timestamp)) {
            return false;
        }
        return testObjectResponseDTOList != null ? testObjectResponseDTOList.equals(that.testObjectResponseDTOList) : that.testObjectResponseDTOList == null;
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + (int) (createObjectCount ^ (createObjectCount >>> 32));
        result = 31 * result + (testObjectResponseDTOList != null ? testObjectResponseDTOList.hashCode() : 0);
        return result;
    }
}
