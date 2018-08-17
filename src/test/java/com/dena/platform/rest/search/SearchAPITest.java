package com.dena.platform.rest.search;

import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import com.dena.platform.rest.dto.ObjectModelHelper;
import com.dena.platform.rest.persistence.store.AbstractDataStoreTest;
import com.dena.platform.utils.CommonConfig;
import org.junit.Before;
import org.junit.Ignore;

import javax.annotation.Resource;

import static org.junit.Assert.assertNotNull;

/**
 * @author Nazarpour.
 */
@Ignore
public class SearchAPITest extends AbstractDataStoreTest {

    private User user = ObjectModelHelper.getSampleUser();

    @Resource
    private DenaUserManagement userManagement;

    @Before
    public void setUp() throws Exception {
        userManagement.registerUser(CommonConfig.APP_NAME, this.user);
    }

//    @Test
//    public void createObjectThenSearchForIt() throws Exception {
//        TokenGenResponse tokenResp = performLoginUser(createJSONFromObject(user), HttpStatus.OK, TokenGenResponse.class);
//
//        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
//        requestObject.addProperty("name", "reza");
//        requestObject.addProperty("job", "developer");
//        requestObject.setActorUsername(user.getEmail());
//
//        DenaResponse actualReturnObject = performCreateObjectWithToken(createJSONFromObject(requestObject), HttpStatus.OK.value(),
//                DenaResponse.class,
//                tokenResp.getToken());
//
//        assertNotNull(actualReturnObject);
//
//        DenaResponse searchReturnObject = performSearchWithToken(user.getEmail(), "name:reza", DenaResponse.class,
//                tokenResp.getToken());
//
//        assertNotNull(searchReturnObject);
//        assertNotNull(searchReturnObject.equals(actualReturnObject));
//    }
}
