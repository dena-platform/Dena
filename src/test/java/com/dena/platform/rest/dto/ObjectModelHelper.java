package com.dena.platform.rest.dto;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.user.domain.User;

/**
 * @author Nazarpour.
 */
public class ObjectModelHelper {

    public static User getSampleUser() {
        return User.UserBuilder.anUser()
                .withEmail("ali@hotmail.com")
                .withPassword(SecurityUtil.encodePassword("123"))
                .withUnencodedPassword("123")
                .build();
    }

    public static DenaObject getSampleDenaObject() {
        DenaObject object = new DenaObject();

        object.setObjectId("6b316b1b4e5f450104c31909");
        object.addField("name", "رضا");
        object.addField("age", "30");
        object.addField("job_title", "برنامه");

        return object;
    }

    public static DenaObject getSecondSampleDenaObject() {
        DenaObject object = new DenaObject();

        object.setObjectId("6b316b1b4e5f450104c31366");
        object.addField("name", "علی");
        object.addField("job_title", "برنامه");

        return object;
    }
}
