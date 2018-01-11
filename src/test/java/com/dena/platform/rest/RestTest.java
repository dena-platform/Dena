package com.dena.platform.rest;

import com.dena.platform.rest.dto.ReturnedObject;
import com.dena.platform.rest.dto.TestObjectResponse;
import com.dena.platform.rest.dto.TestRelatedObject;
import com.dena.platform.rest.dto.TestRequestObject;
import com.dena.platform.utils.CommonConfig;
import com.mongodb.MongoClient;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
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
        testObjectResponse.addProperty("job", "new developer");
        testObjectResponse.addProperty("name", "developer");
        testObjectResponse.testRelatedObjects = Collections.singletonList(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME));


        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(testObjectResponse));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

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
