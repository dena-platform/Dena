package com.dena.platform.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class DenaRelation {

    @JsonProperty(value = "relation_name", required = true)
    private String relationName;

    @JsonProperty(value = "relation_type", required = true)
    private String relationType;

    @JsonProperty(value = "target_name", required = true)
    private String targetName;

    @JsonProperty(value = "ids", required = true)
    private List<String> ids = new ArrayList<>();


    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }


    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DenaRelation that = (DenaRelation) o;
        return Objects.equals(relationName, that.relationName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(relationName);
    }

    @Override
    public String toString() {
        return "DenaRelation{" +
                "relationName='" + relationName + '\'' +
                ", type='" + relationType + '\'' +
                ", targetName='" + targetName + '\'' +
                ", ids=" + ids +
                '}';
    }
}
