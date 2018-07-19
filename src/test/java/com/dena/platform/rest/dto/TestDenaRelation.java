package com.dena.platform.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class TestDenaRelation {
    @JsonProperty(value = "relation_name", required = true)
    public String relationName;

    @JsonProperty(value = "relation_type", required = true)
    public String relationType;

    @JsonProperty(value = "target_name", required = true)
    public String targetName;

    @JsonProperty(value = "ids", required = true)
    public List<String> ids = new ArrayList<>();

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }


    public static final class TestDenaRelationDTOBuilder {
        private TestDenaRelation testDenaRelation;

        private TestDenaRelationDTOBuilder() {
            testDenaRelation = new TestDenaRelation();
        }

        public static TestDenaRelationDTOBuilder aTestDenaRelationDTO() {
            return new TestDenaRelationDTOBuilder();
        }

        public TestDenaRelationDTOBuilder withRelationName(String relationName) {
            testDenaRelation.setRelationName(relationName);
            return this;
        }

        public TestDenaRelationDTOBuilder withRelationType(String relationType) {
            testDenaRelation.setRelationType(relationType);
            return this;
        }

        public TestDenaRelationDTOBuilder withTargetName(String targetName) {
            testDenaRelation.setTargetName(targetName);
            return this;
        }

        public TestDenaRelationDTOBuilder withIds(String... ids) {
            testDenaRelation.setIds(Arrays.asList(ids));
            return this;
        }

        public TestDenaRelation build() {
            return testDenaRelation;
        }
    }
}
