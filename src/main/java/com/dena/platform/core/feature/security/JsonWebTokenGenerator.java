package com.dena.platform.core.feature.security;

import com.dena.platform.core.feature.user.DenaUserManagement;
import com.dena.platform.core.feature.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Nazarpour.
 */
@Service("jwtGenerator")
public class JsonWebTokenGenerator implements TokenGenerator {
    private final static Logger log = LoggerFactory.getLogger(JsonWebTokenGenerator.class);

    @Resource
    private DenaUserManagement userManagement;

    public String generate(String appId, User claimedUser) {
        String username = claimedUser.getEmail();
        String password = claimedUser.getPassword();
        User user = userManagement.getUserById(appId, username);

        if (user != null && SecurityUtil.matchesPassword(password, user.getPassword())) {
            Claims claims = Jwts.claims()
                    .setSubject(username);

            claims.put("role", "fixed_role"); //TODO change role to user role

            return Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS512, "S3CREt") //TODO change secret to
                    .compact();
        } else {
            throw new AuthenticationServiceException(String.format("not authenticated user: %s", username));
        }
    }
}
