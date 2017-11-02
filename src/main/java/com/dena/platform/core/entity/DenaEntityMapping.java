package com.dena.platform.core.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public enum DenaEntityMapping {
    BaseLongIdEntity(BaseLongIdEntity.class.getSimpleName(), BaseLongIdEntity.class),
    BaseUuiddEntity(BaseUuidEntity.class.getSimpleName(), BaseUuidEntity.class);

    private String name;
    private Class<? extends BaseEntity> klass;

    private static final Map<String, Class<? extends BaseEntity>> ENTITY_MAPPING = new HashMap<>();

    static {
        for (DenaEntityMapping denaEntityMapping : EnumSet.allOf(DenaEntityMapping.class))
            ENTITY_MAPPING.put(denaEntityMapping.name, denaEntityMapping.klass);
    }


    DenaEntityMapping(String name, Class<? extends BaseEntity> klass) {
        this.name = name;
        this.klass = klass;

    }

    public static Class getKlass(String klassName) {
        return ENTITY_MAPPING.get(klassName);
    }
}
