package com.dena.platform.rest.app;

import com.dena.platform.rest.dto.TestErrorResponseDTO;
import com.dena.platform.rest.dto.TestObjectResponseDTO;
import com.dena.platform.rest.dto.TestRequestObjectDTO;
import com.dena.platform.rest.persistence.AbstractDataStoreTest;
import com.dena.platform.restapi.dto.response.DenaResponse;
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
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class ApplicationManagement extends AbstractDataStoreTest {
    @Test
    public void test_registerNewApplication() throws Exception {

        /////////////////////////////////////////////
        //     Send Register New Application Request
        /////////////////////////////////////////////
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("application_name", "great_app");
        requestObject.addProperty("creator_id", "developer@dena.com");

        DenaResponse actualReturnObject = performRegisterNewApplication(createJSONFromObject(requestObject), HttpStatus.OK, DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Register User Response
        /////////////////////////////////////////////
        TestObjectResponseDTO expectedObjectResponse = new TestObjectResponseDTO();
        expectedObjectResponse.objectId = actualReturnObject.getDenaObjectResponseList().get(0).getObjectId();
        expectedObjectResponse.objectURI = "/DENA_APPLICATION/" + expectedObjectResponse.objectId;
        expectedObjectResponse.createTime = actualReturnObject.getDenaObjectResponseList().get(0).getCreateTime();
        expectedObjectResponse.updateTime = null;

        expectedObjectResponse.addProperty("password", "123456");
        expectedObjectResponse.addProperty("is_active", true);
        expectedObjectResponse.addProperty("email", "user2@denaplatform.com");
        expectedObjectResponse.addProperty("name", "alex");
        expectedObjectResponse.addProperty("family", "smith");

//        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);


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
