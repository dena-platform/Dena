package com.dena.platform.core.feature.security.login_delete;

import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.user.DenaUserManagement;
import com.dena.platform.core.feature.user.domain.User;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final static Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Resource(name = "denaLoginService")
    private DenaLoginService denaLoginService;

    @Resource(name = "denaDenaUserManagementImpl")
    private DenaUserManagement userManagement;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            String requestBody = IOUtils.toString(request.getReader());
            HashMap<String, Object> requestMap = JSONMapper.createHashMapFromJSON(requestBody);

            String username = (String) requestMap.get("username");
            String password = (String) requestMap.get("password");
            String appId = (String) requestMap.get("app_id");
            User user = userManagement.getUserById(appId, username);

            if (user != null && SecurityUtil.matchesPassword(password, user.getPassword())) {
                //authenticated
            } else {
                //not authenticated
            }

        } catch (IOException ex) {
            log.error("Error in parsing credential");
        }
        return null;
    }
}
