package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.dto.DenaRelation;
import com.dena.platform.core.feature.persistence.mongodb.MongoDBDataStoreImpl;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoDBDataStoreImplTest {

    @Resource
    private MongoDBDataStoreImpl mongoDBDataStore;

    @Resource
    protected MongoClient mongoClient;

    private final String SAMPLE_TABLE_NAME = "table1";


    @Before
    public void setUp() throws Exception {
        mongoClient.getDatabase(CommonConfig.APP_NAME).drop();
    }

    @Test
    public void test_store() {
        // given
        DenaObject denaObject = new DenaObject();
        denaObject.addField("name", "alex");
        denaObject.addField("family", "smith");

        // when
        List<DenaObject> storedObject = mongoDBDataStore.store(CommonConfig.APP_NAME, SAMPLE_TABLE_NAME, denaObject);
        final String storedObjectId = storedObject.get(0).getObjectId();
        DenaObject foundObject = mongoDBDataStore.find(CommonConfig.APP_NAME, SAMPLE_TABLE_NAME, storedObjectId).get(0);

        // then
        Assertions.assertThat(foundObject.getObjectId()).isNotBlank();
        Assert.assertNotNull("Creation time should not be null", foundObject.getCreateTime());
        Assert.assertNull("Update time should be null", foundObject.getUpdateTime());
        Assert.assertEquals("alex", foundObject.getField("name", String.class));
        Assert.assertEquals("smith", foundObject.getField("family", String.class));


        List<DenaObject> emptyObject = mongoDBDataStore.store(CommonConfig.APP_NAME, SAMPLE_TABLE_NAME);
        Assert.assertEquals("Should return empty collection when we did not send any object.", 0, emptyObject.size());

    }

    @Test
    public void test_mergeUpdate() {
        // given
        DenaObject denaObject = new DenaObject();
        denaObject.addField("name", "alex");
        denaObject.addField("family", "smith");

        // when
        DenaObject storedObject = mongoDBDataStore.store(CommonConfig.APP_NAME, SAMPLE_TABLE_NAME, denaObject).get(0);
        storedObject.addField("car_number", 1234);
        storedObject.addField("car_name", "Peugeot 206");
        storedObject.addField("family", "Williams");

        DenaObject mergedObject = mongoDBDataStore.mergeUpdate(CommonConfig.APP_NAME, SAMPLE_TABLE_NAME, storedObject).get(0);

        // then
        Assertions.assertThat(mergedObject.getObjectId()).isNotBlank();
        Assert.assertNotNull("Creation time should not be null", mergedObject.getCreateTime());
        Assert.assertNotNull("Update time should not be null", mergedObject.getUpdateTime());
        Assert.assertEquals("alex", mergedObject.getField("name", String.class));
        Assert.assertEquals("Williams", mergedObject.getField("family", String.class));
        Assert.assertEquals(1234, (int) mergedObject.getField("car_number", Integer.class));
        Assert.assertEquals("Peugeot 206", mergedObject.getField("car_name", String.class));

    }

    @Test
    public void test_replaceUpdate() {
        // given
        DenaObject denaObject = new DenaObject();
        denaObject.addField("name", "alex");
        denaObject.addField("family", "smith");

        // when
        DenaObject storedObject = mongoDBDataStore.store(CommonConfig.APP_NAME, SAMPLE_TABLE_NAME, denaObject).get(0);

        DenaObject newDenaObject = new DenaObject();
        newDenaObject.setObjectId(storedObject.getObjectId());
        newDenaObject.addField("car_number", 1234);
        newDenaObject.addField("car_name", "Peugeot 206");
        newDenaObject.addField("family", "Williams");

        DenaObject replacedObject = mongoDBDataStore.replaceUpdate(CommonConfig.APP_NAME, SAMPLE_TABLE_NAME, newDenaObject).get(0);

        // then
        Assertions.assertThat(replacedObject.getObjectId()).isNotBlank();
        Assert.assertNotNull("Creation time should not be null", replacedObject.getCreateTime());
        Assert.assertNotNull("Update time should not be null", replacedObject.getUpdateTime());
        Assert.assertNull("'name' field should be deleted in replace update.",
                replacedObject.getField("name", String.class));
        Assert.assertEquals("Williams", replacedObject.getField("family", String.class));
        Assert.assertEquals(1234, (int) replacedObject.getField("car_number", Integer.class));
        Assert.assertEquals("Peugeot 206", replacedObject.getField("car_name", String.class));

    }

    @Test
    public void test_delete() {
        // given
        DenaObject denaObject = new DenaObject();
        denaObject.addField("name", "alex");
        denaObject.addField("family", "smith");

        // when
        DenaObject storedObject = mongoDBDataStore.store(CommonConfig.APP_NAME, SAMPLE_TABLE_NAME, denaObject).get(0);
        String storedObjectId = storedObject.getObjectId();

        Long numberOfDeleteObject = mongoDBDataStore.delete(CommonConfig.APP_NAME, SAMPLE_TABLE_NAME, storedObjectId);

        // Delete not available object id
        Long shouldBeZeroDeletedCount = mongoDBDataStore.delete(CommonConfig.APP_NAME, SAMPLE_TABLE_NAME,
                "5a316b1b4e5f450104c31909");


        // then
        Assertions.assertThat(numberOfDeleteObject).isEqualTo(1);
        Assertions.assertThat(shouldBeZeroDeletedCount).isEqualTo(0);


    }

    @Test
    public void test_delete_relation() {
        // given
        final String SAMPLE_RELATION_NAME = "rel1";
        final String SAMPLE_TARGET_NAME = "rel1";

        DenaObject denaObject = new DenaObject();
        denaObject.addField("name", "alex");
        denaObject.addField("family", "smith");

        DenaRelation denaRelation = new DenaRelation();
        denaRelation.setTargetTableName(SAMPLE_TARGET_NAME);
        denaRelation.setRelationName(SAMPLE_RELATION_NAME);
        denaRelation.setIds(
                Arrays.asList("5a316b1b4e5f450104c31909",
                        "5a316b1b4e5f450104c31100",
                        "5a316b1b4e5f450104c31911")
        );
        denaObject.setDenaRelations(Collections.singletonList(denaRelation));


        // when
        DenaObject storedObject = mongoDBDataStore
                .store(CommonConfig.APP_NAME, SAMPLE_TABLE_NAME, denaObject)
                .get(0);


        final String storedObjectId = storedObject.getObjectId();

        Long numberOfDeleteRelation = mongoDBDataStore.deleteRelation(CommonConfig.APP_NAME,
                SAMPLE_TABLE_NAME, storedObjectId,
                SAMPLE_RELATION_NAME);

        // then
        Assertions.assertThat(numberOfDeleteRelation).isEqualTo(1);
    }

}
