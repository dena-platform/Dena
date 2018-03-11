package com.dena.platform.core.feature.persistence;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public enum RelationType {
    RELATION_1_TO_1("ONE-TO-ONE"),
    RELATION_1_TO_N("ONE-TO-MANY");

    public final String name;


    RelationType(String name) {
        this.name = name;
    }


}
