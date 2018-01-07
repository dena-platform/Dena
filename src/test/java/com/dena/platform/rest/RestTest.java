package com.dena.platform.rest;

import com.dena.platform.rest.dto.*;
import com.dena.platform.utils.CommonConfig;
import com.mongodb.MongoClient;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTest {

    private String objectId1 = "5a316b1b4e5f450104c31909";
    private String objectId2 = "5a1bd6176f017921441d4a50";
    private String objectId3 = "5a206dafcc2a9b26e483d663";
    private final String randomObjectId = ObjectId.get().toHexString();


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

        mongoClient.getDatabase(CommonConfig.APP_ID).drop();

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


        mongoClient.getDatabase(CommonConfig.APP_ID)
                .getCollection(CommonConfig.COLLECTION_NAME)
                .insertOne(document1);

        mongoClient.getDatabase(CommonConfig.APP_ID)
                .getCollection(CommonConfig.COLLECTION_NAME)
                .insertOne(document2);

        mongoClient.getDatabase(CommonConfig.APP_ID)
                .getCollection(CommonConfig.COLLECTION_NAME)
                .insertOne(document3);

    }

    @Test
    public void test_FindObject_When_Object_Exist() throws Exception {

        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        ReturnedObject actualReturnObject = performFindRequest(objectId3);

        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.objectId = objectId3;
        testObjectResponse.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId3;
        testObjectResponse.addProperty("name", "javad");
        testObjectResponse.addProperty("job", "developer");
        testObjectResponse.testRelatedObjects = Arrays.asList(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME), new TestRelatedObject(objectId2, CommonConfig.COLLECTION_NAME));
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(testObjectResponse));

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_FindRelatedObject() throws Exception {
        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        ReturnedObject actualReturnObject = performFindRelationRequest(objectId3, CommonConfig.COLLECTION_NAME);

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setCount(2L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());


        TestObjectResponse testObjectResponse1 = new TestObjectResponse();
        testObjectResponse1.objectId = objectId1;
        testObjectResponse1.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId1;
        testObjectResponse1.addProperty("name", "javad");
        testObjectResponse1.addProperty("job", "developer");

        TestObjectResponse testObjectResponse2 = new TestObjectResponse();
        testObjectResponse2.objectId = objectId2;
        testObjectResponse2.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId2;
        testObjectResponse2.addProperty("name", "javad");
        testObjectResponse2.addProperty("job", "developer");

        expectedReturnObject.setTestObjectResponseList(Arrays.asList(testObjectResponse1, testObjectResponse2));


        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }

    @Test
    public void test_FindObject_When_Object_Not_Exist() throws Exception {

        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        ReturnedObject actualReturnObject = performFindRequest(randomObjectId);

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setCount(0L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), JSONCompareMode.NON_EXTENSIBLE);

    }



    @Test
    public void test_UpdateObject() throws Exception {
        /////////////////////////////////////////////
        //           Send Update Object Request
        /////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.setObjectId(objectId3);
        requestObject.addProperty("job", "new developer");
        requestObject.addProperty("new field", "new value");
        String newObjectId = ObjectId.get().toString();
        requestObject.getRelatedObjects().add(new TestRelatedObject(newObjectId, CommonConfig.COLLECTION_NAME));

        ReturnedObject actualReturnObject = performUpdateObject(createJSONFromObject(requestObject));

        /////////////////////////////////////////////
        //            Assert Update Response
        /////////////////////////////////////////////
        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.objectId = "5a206dafcc2a9b26e483d663";
        testObjectResponse.objectURI = "/denaTestCollection/5a206dafcc2a9b26e483d663";
        testObjectResponse.getAllFields().put("job", "new developer");
        testObjectResponse.getAllFields().put("new field", "new value");
        testObjectResponse.testRelatedObjects = Collections.singletonList(new TestRelatedObject(newObjectId, CommonConfig.COLLECTION_NAME));


        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(testObjectResponse));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }

    @Test
    public void test_CreateObject() throws Exception {
        /////////////////////////////////////////////
        //           Send Create Object Request
        /////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.addProperty("job", "new developer");
        requestObject.addProperty("name", "developer");
        requestObject.getRelatedObjects().add(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME));

        ReturnedObject actualReturnObject = performCreateObject(createJSONFromObject(requestObject));

        /////////////////////////////////////////////
        //            Assert Create Response
        /////////////////////////////////////////////
        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.getAllFields().put("job", "new developer");
        testObjectResponse.getAllFields().put("name", "developer");
        testObjectResponse.testRelatedObjects = Collections.singletonList(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME));


        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(testObjectResponse));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }


    private ReturnedObject performFindRequest(String objectId1) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.BASE_URL + "/" + objectId1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ReturnedObject.class);
    }

    private ReturnedObject performFindRelationRequest(String objectId1, String targetType) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.BASE_URL + "/" + objectId1 + "/relation/" + targetType))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ReturnedObject.class);
    }

    private <T> T performDeleteRequest(List<String> objectList, int status, Class<T> klass) throws Exception {
        return performDeleteRequest(objectList, CommonConfig.BASE_URL + "/", status, klass);
    }

    private <T> T performDeleteRequest(List<String> objectList, String urlRequest, int status, Class<T> klass) throws Exception {
        String objectIds = String.join(",", objectList);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(urlRequest + objectIds))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);
    }

    private ReturnedObject performDeleteRelationWithObject(String parentObjectId, String relationName, String targetObjectId) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(CommonConfig.BASE_URL + "/" + parentObjectId + "/relation/" + relationName + "/" + targetObjectId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ReturnedObject.class);

    }

    private ReturnedObject performDeleteRelation(String type1, String relationName) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(CommonConfig.BASE_URL + "/" + type1 + "/relation/" + relationName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ReturnedObject.class);

    }

    private ReturnedObject performUpdateObject(String body) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ReturnedObject.class);

    }

    private ReturnedObject performCreateObject(String body) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ReturnedObject.class);

    }


}
