package com.dena.platform.rest.app;

import com.dena.platform.rest.dto.TestDenaResponse;
import com.dena.platform.rest.dto.TestErrorResponse;
import com.dena.platform.rest.dto.TestObjectResponse;
import com.dena.platform.rest.dto.TestRequestObject;
import com.dena.platform.rest.persistence.AbstractDataStoreTest;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.restapi.dto.response.ErrorResponse;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.JSONMapper.createObjectFromJSON;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class ApplicationManagement extends AbstractDataStoreTest {
    @Test
    public void test_registerNewApplication() throws Exception {

        /////////////////////////////////////////////
        //     Send Register New Application Request
        /////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.addProperty("application_name", "great_app");
        requestObject.addProperty("creator_id", "developer@dena.com");

        DenaResponse actualReturnObject = performRegisterNewApplication(createJSONFromObject(requestObject), HttpStatus.OK, DenaResponse.class);

        /////////////////////////////////////////////
        //     Assert Register User Response
        /////////////////////////////////////////////

        String application_id = (String) actualReturnObject.getDenaObjectResponseList().get(0).getFields().get("application_id");
        String secretKey = (String) actualReturnObject.getDenaObjectResponseList().get(0).getFields().get("secret_key");

        TestObjectResponse expectedObjectResponse = new TestObjectResponse();
        expectedObjectResponse.objectId = actualReturnObject.getDenaObjectResponseList().get(0).getObjectId();
        expectedObjectResponse.objectURI = "/DENA_APPLICATION_INFO/" + expectedObjectResponse.objectId;
        expectedObjectResponse.createTime = actualReturnObject.getDenaObjectResponseList().get(0).getCreateTime();
        expectedObjectResponse.updateTime = null;

        expectedObjectResponse.addProperty("secret_key", secretKey);
        expectedObjectResponse.addProperty("application_name", "great_app");
        expectedObjectResponse.addProperty("creator_id", "developer@dena.com");
        expectedObjectResponse.addProperty("application_id", application_id);

        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();
        expectedReturnObject.createObjectCount = 1L;
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(expectedObjectResponse));


        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);


    }

    @Test
    public void test_registerNewApplication_When_App_Exist() throws Exception {

        /////////////////////////////////////////////
        //     Send Register New Application Request
        /////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.addProperty("application_name", "great_app");
        requestObject.addProperty("creator_id", "developer@dena.com");

        DenaResponse okReturn = performRegisterNewApplication(createJSONFromObject(requestObject), HttpStatus.OK, DenaResponse.class);
        ErrorResponse errorReturn = performRegisterNewApplication(createJSONFromObject(requestObject), HttpStatus.BAD_REQUEST, ErrorResponse.class);

        /////////////////////////////////////////////
        //     Assert Register User Response
        /////////////////////////////////////////////
        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "4002";
        expectedReturnObject.messages = Collections.singletonList("Application already exist");



        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(errorReturn), true);


    }


    @Test
    public void test_registerNewApplication_When_App_Name_Is_Invalid() throws Exception {
        /////////////////////////////////////////////
        //     Send Register New Application Request
        /////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.addProperty("application_name", "as");   // application name length is invalid
        requestObject.addProperty("creator_id", "developer@dena.com");

        ErrorResponse actualReturnObject = performRegisterNewApplication(createJSONFromObject(requestObject), HttpStatus.BAD_REQUEST, ErrorResponse.class);

        /////////////////////////////////////////////
        //     Assert Register User Response
        /////////////////////////////////////////////
        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "4000";
        expectedReturnObject.messages = Collections.singletonList("Application name field is invalid");



        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }




        /////////////////////////////////////////////////
    //            Application MANAGEMENT REQUEST
    /////////////////////////////////////////////////
    protected <T> T performRegisterNewApplication(String body, HttpStatus httpStatus, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.REGISTER_APPLICATION_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(httpStatus.value()))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }


}
