package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.mongodb.MongoDBSchemaManagerImpl;
import com.dena.platform.core.feature.persistence.mongodb.MongoDBUtils;
import com.dena.platform.utils.CommonConfig;
import com.mongodb.MongoClient;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoDBSchemaManagerImplIT {

    @Resource
    protected MongoClient mongoClient;

    @Resource
    private MongoDBSchemaManagerImpl mongoDBSchemaManager;


    @Before
    public void setUp() {
        mongoClient.getDatabase(CommonConfig.APP_NAME).drop();
    }


    @Test
    public void test_createSchema() {
        final String SAMPLE_SCHEMA_NAME = "SCHEMA1";

        int numberOFCreatedSchema = mongoDBSchemaManager.createSchema(CommonConfig.APP_NAME, SAMPLE_SCHEMA_NAME);
        Assert.assertEquals(1, numberOFCreatedSchema);
    }

    @Test
    public void test_findAllSchema() {
        final String SAMPLE_SCHEMA_NAME1 = "SCHEMA1";
        final String SAMPLE_SCHEMA_NAME2 = "SCHEMA2";

        int numberOFCreatedSchema1 = mongoDBSchemaManager.createSchema(CommonConfig.APP_NAME, SAMPLE_SCHEMA_NAME1);
        int numberOFCreatedSchema2 = mongoDBSchemaManager.createSchema(CommonConfig.APP_NAME, SAMPLE_SCHEMA_NAME2);

        List<DenaObject> foundSchema = mongoDBSchemaManager.findAllSchema(CommonConfig.APP_NAME);


        Assert.assertEquals("SCHEMA1", foundSchema.get(0).getField("name", String.class));
        Assertions.assertThat(foundSchema.get(0).getField("record_count(s)", Long.class)).isEqualTo(0);

        Assert.assertEquals("SCHEMA2", foundSchema.get(1).getField("name", String.class));
        Assertions.assertThat(foundSchema.get(1).getField("record_count(s)", Long.class)).isEqualTo(0);

        Assert.assertEquals(1, numberOFCreatedSchema1);
        Assert.assertEquals(1, numberOFCreatedSchema2);

    }

    @Test
    public void test_deleteSchema() {
        final String SAMPLE_SCHEMA_NAME1 = "SCHEMA1";
        final String SAMPLE_SCHEMA_NAME2 = "SCHEMA2";

        mongoDBSchemaManager.createSchema(CommonConfig.APP_NAME, SAMPLE_SCHEMA_NAME1);
        mongoDBSchemaManager.createSchema(CommonConfig.APP_NAME, SAMPLE_SCHEMA_NAME2);

        int numberOFDeletedSchema = mongoDBSchemaManager.deleteSchema(CommonConfig.APP_NAME, SAMPLE_SCHEMA_NAME1);

        Assert.assertEquals(1, numberOFDeletedSchema);
        Assert.assertFalse(MongoDBUtils.isSchemaExist(mongoClient.getDatabase(CommonConfig.APP_NAME),
                SAMPLE_SCHEMA_NAME1));


    }

}
