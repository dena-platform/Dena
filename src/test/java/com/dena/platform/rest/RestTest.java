package com.dena.platform.rest;

import com.dena.platform.rest.dto.DenaObject;
import com.dena.platform.rest.dto.ExpectedReturnedObject;
import com.dena.platform.rest.dto.RelatedObject;
import com.dena.platform.utils.CommonConfig;
import com.mongodb.MongoClient;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.JSONMapper.createObjectFromJSON;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfMinute;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTest {

    private final static Logger log = LoggerFactory.getLogger(RestTest.class);

    private String objectId1 = "5a316b1b4e5f450104c31909";
    private String objectId2 = "5a1bd6176f017921441d4a50";
    private String objectId3 = "5a206dafcc2a9b26e483d663";


    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext wac;

    @Resource
    private MongoClient mongoClient;


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();


        //////////////////////////////////////////////////////
        //       Initialize database
        //////////////////////////////////////////////////////


        mongoClient.getDatabase(CommonConfig.DB_NAME).drop();

        Document document1 = new Document();

        document1.put("_id", new ObjectId(objectId1));
        document1.put("name", "javad");
        document1.put("job", "developer");

        Document document2 = new Document();

        document2.put("_id", new ObjectId(objectId2));
        document2.put("name", "javad");
        document2.put("job", "developer");


        Document document3 = new Document();
        document3.put("_id", new ObjectId(objectId3));
        document3.put("name", "javad");
        document3.put("job", "developer");
        document3.put(CommonConfig.COLLECTION_NAME, Arrays.asList(new ObjectId(objectId1), new ObjectId(objectId2)));


        mongoClient.getDatabase(CommonConfig.DB_NAME)
                .getCollection(CommonConfig.COLLECTION_NAME)
                .insertOne(document1);

        mongoClient.getDatabase(CommonConfig.DB_NAME)
                .getCollection(CommonConfig.COLLECTION_NAME)
                .insertOne(document2);

        mongoClient.getDatabase(CommonConfig.DB_NAME)
                .getCollection(CommonConfig.COLLECTION_NAME)
                .insertOne(document3);

    }


    @Test
    public void testFindObjectWhenObjectExist() throws Exception {

        //////////////////////////////////////////////////////
        //       FIND OBJECT WITH NO RELATION
        //////////////////////////////////////////////////////
        ExpectedReturnedObject actualReturnObject = performFindRequest(objectId1);

        ExpectedReturnedObject expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        DenaObject denaObject = new DenaObject();
        denaObject.objectId = objectId1;
        denaObject.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId1;
        denaObject.addProperty("name", "javad");
        denaObject.addProperty("job", "developer");
        expectedReturnObject.setDenaObjectList(Collections.singletonList(denaObject));

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfMinute(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);


        //////////////////////////////////////////////////////
        //       FIND OBJECT WITH RELATION
        //////////////////////////////////////////////////////
        actualReturnObject = performFindRequest(objectId3);

        expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        denaObject = new DenaObject();
        denaObject.objectId = objectId3;
        denaObject.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId3;
        denaObject.addProperty("name", "javad");
        denaObject.addProperty("job", "developer");
        denaObject.relatedObjects = Arrays.asList(new RelatedObject(objectId1, CommonConfig.COLLECTION_NAME), new RelatedObject(objectId2, CommonConfig.COLLECTION_NAME));
        expectedReturnObject.setDenaObjectList(Collections.singletonList(denaObject));

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfMinute(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void testFindObjectWhenObjectNotExist() throws Exception {
        String randomObjectId = ObjectId.get().toHexString();

        ExpectedReturnedObject actualReturnObject = performFindRequest(randomObjectId);

        ExpectedReturnedObject expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setCount(0L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfMinute(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_DeleteObjects_When_Object_Exist() throws Exception {
        ExpectedReturnedObject actualReturnObject = performDeleteRequest(Arrays.asList(objectId1, objectId2, objectId3));

        ExpectedReturnedObject expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(3L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfMinute(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }


    @Test
    public void test_DeleteObject_When_Object_Not_Exist() throws Exception {
        String randomObjectId = ObjectId.get().toHexString();
        ExpectedReturnedObject actualReturnObject = performDeleteRequest(Collections.singletonList(randomObjectId));

        ExpectedReturnedObject expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(0L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfMinute(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_DeleteRelation_With_Object_When_Object_Exist() throws Exception {
        /////////////////////////////////////////////
        //            Delete Relation
        /////////////////////////////////////////////
        ExpectedReturnedObject actualReturnObject = performDeleteRelationWithObject(objectId3, CommonConfig.COLLECTION_NAME, objectId1);

        /////////////////////////////////////////////
        //            Assert Delete Response
        /////////////////////////////////////////////
        ExpectedReturnedObject expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        assertTrue(isTimeEqualRegardlessOfMinute(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

        /////////////////////////////////////////////
        //            Check Response Find
        /////////////////////////////////////////////
        actualReturnObject = performFindRequest(objectId3);
        expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        DenaObject denaObject = new DenaObject();
        denaObject.objectId = objectId3;
        denaObject.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId3;
        denaObject.addProperty("name", "javad");
        denaObject.addProperty("job", "developer");
        denaObject.relatedObjects = Collections.singletonList(new RelatedObject(objectId2, CommonConfig.COLLECTION_NAME));
        expectedReturnObject.setDenaObjectList(Collections.singletonList(denaObject));

        assertTrue(isTimeEqualRegardlessOfMinute(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_DeleteRelation_When_Exist() throws Exception {
        /////////////////////////////////////////////
        //            Delete Relation
        /////////////////////////////////////////////
        ExpectedReturnedObject actualReturnObject = performDeleteRelationWithObject(objectId3, CommonConfig.COLLECTION_NAME, objectId1);

        /////////////////////////////////////////////
        //            Assert Delete Response
        /////////////////////////////////////////////
        ExpectedReturnedObject expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        assertTrue(isTimeEqualRegardlessOfMinute(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

        /////////////////////////////////////////////
        //            Check Response Find
        /////////////////////////////////////////////
        actualReturnObject = performFindRequest(objectId3);
        expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        DenaObject denaObject = new DenaObject();
        denaObject.objectId = objectId3;
        denaObject.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId3;
        denaObject.addProperty("name", "javad");
        denaObject.addProperty("job", "developer");
        denaObject.relatedObjects = Collections.singletonList(new RelatedObject(objectId2, CommonConfig.COLLECTION_NAME));
        expectedReturnObject.setDenaObjectList(Collections.singletonList(denaObject));

        assertTrue(isTimeEqualRegardlessOfMinute(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }


    private ExpectedReturnedObject performFindRequest(String objectId1) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.BASE_URL + "/" + objectId1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ExpectedReturnedObject.class);
    }

    private ExpectedReturnedObject performDeleteRequest(List<String> objectList) throws Exception {
        String objectIds = String.join(",", objectList);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(CommonConfig.BASE_URL + "/" + objectIds))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ExpectedReturnedObject.class);
    }

    private ExpectedReturnedObject performDeleteRelationWithObject(String type1, String relationName, String type2) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(CommonConfig.BASE_URL + "/" + type1 + "/relation/" + relationName + "/" + type2))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ExpectedReturnedObject.class);

    }

    private ExpectedReturnedObject performDeleteRelation(String type1, String relationName) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(CommonConfig.BASE_URL + "/" + type1 + "/relation/" + relationName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ExpectedReturnedObject.class);

    }


}
