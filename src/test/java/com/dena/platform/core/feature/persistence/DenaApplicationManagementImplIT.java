package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.app.domain.DenaApplication;
import com.dena.platform.core.feature.app.service.DenaApplicationManagementImpl;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class DenaApplicationManagementImplIT {

    @Resource
    private DenaApplicationManagementImpl denaApplicationManagement;

    @Resource
    protected MongoClient mongoClient;


    private final String SAMPLE_APP_NAME = "app_name_1";
    private final String SAMPLE_CREATOR_ID = "email@sample.com";

    @Before
    public void setUp() {
        mongoClient.getDatabase("DENA_APPLICATIONS")
                .getCollection("DENA_APPLICATION_INFO")
                .drop();
    }


    @Test
    public void test_registerApplication() {


        DenaApplication denaApplication = DenaApplication.DenaAPPBuilder.aDenaAPP()
                .withApplicationName(SAMPLE_APP_NAME)
                .withCreatorId(SAMPLE_CREATOR_ID)
                .build();

        DenaObject registerApplication = denaApplicationManagement.registerApplication(denaApplication);

        Assert.assertEquals(SAMPLE_CREATOR_ID, registerApplication.getField(DenaApplication.CREATOR_ID_FIELD, String.class));
        Assert.assertEquals(SAMPLE_APP_NAME, registerApplication.getField(DenaApplication.APP_NAME_FIELD, String.class));
    }

    @Test
    public void test_findApplicationByName() {

        DenaApplication denaApplication = DenaApplication.DenaAPPBuilder.aDenaAPP()
                .withApplicationName(SAMPLE_APP_NAME)
                .withCreatorId(SAMPLE_CREATOR_ID)
                .build();

        denaApplicationManagement.registerApplication(denaApplication);

        Optional<DenaObject> foundApplication = denaApplicationManagement
                .findApplicationByName(SAMPLE_CREATOR_ID, SAMPLE_APP_NAME);

        Assert.assertTrue(foundApplication.isPresent());

    }

    @Test
    public void test_findApplicationById() {
        DenaApplication denaApplication = DenaApplication.DenaAPPBuilder.aDenaAPP()
                .withApplicationName(SAMPLE_APP_NAME)
                .withCreatorId(SAMPLE_CREATOR_ID)
                .build();

        DenaObject registerApplication = denaApplicationManagement.registerApplication(denaApplication);

        Optional<DenaObject> foundApplication = denaApplicationManagement
                .findApplicationById(registerApplication.getField(DenaApplication.APP_ID_FIELD, String.class));

        Assert.assertTrue(foundApplication.isPresent());

    }

    @Test
    public void test_getSecretId() {
        DenaApplication denaApplication = DenaApplication.DenaAPPBuilder.aDenaAPP()
                .withApplicationName(SAMPLE_APP_NAME)
                .withCreatorId(SAMPLE_CREATOR_ID)
                .build();

        DenaObject registerApplication = denaApplicationManagement.registerApplication(denaApplication);

        String foundAppId = denaApplicationManagement
                .getSecretId(registerApplication.getField(DenaApplication.APP_ID_FIELD, String.class));

        String actualAppId = mongoClient
                .getDatabase("DENA_APPLICATIONS")
                .getCollection("DENA_APPLICATION_INFO")
                .find(Filters.eq("_id", new ObjectId(registerApplication.getObjectId())))
                .first()
                .get("secret_key", String.class);


        Assert.assertEquals(actualAppId, foundAppId);
    }

    @Test
    public void test_isApplicationExist() {
        DenaApplication denaApplication = DenaApplication.DenaAPPBuilder.aDenaAPP()
                .withApplicationName(SAMPLE_APP_NAME)
                .withCreatorId(SAMPLE_CREATOR_ID)
                .build();

        denaApplicationManagement.registerApplication(denaApplication);

        boolean isApplicationExist = denaApplicationManagement.isApplicationExist(SAMPLE_CREATOR_ID, SAMPLE_APP_NAME);

        Assert.assertTrue(isApplicationExist);
    }


}

