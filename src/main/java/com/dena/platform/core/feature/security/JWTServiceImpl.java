package com.dena.platform.core.feature.security;

import com.dena.platform.common.config.DenaConfigReader;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Instant;
import java.util.Date;

/**
 * @author Nazarpour.
 */
@Service("jwtService")
public class JWTServiceImpl implements JWTService {
    private final static Logger lof = LoggerFactory.getLogger(JWTServiceImpl.class);

    @Resource
    private DenaUserManagement userManagement;

    private String secret;
    private int tokenExpireDuration;

    @PostConstruct
    private void init() {
        secret = DenaConfigReader.readProperty("dena.security.jwt.token.secret");
        tokenExpireDuration = DenaConfigReader.readIntProperty("dena.security.jwt.token.expire.duration.millis",
                1_800_000);
    }

    @Override
    public String generateJWTToken(String appId, User claimedUser) {
        String username = claimedUser.getEmail();

        Date expireDate = Date.from(Instant.now().plusMillis(tokenExpireDuration));

        Claims claims = Jwts.claims()
                .setSubject(username);
        claims.setExpiration(expireDate);

        claims.put("role", "fixed_role"); //TODO change role to user role
        claims.put("app_id", appId);
        claims.put("userName", username);
        claims.put("creation_date", Instant.now());


        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        claimedUser.setToken(token);
        userManagement.updateUser(appId, claimedUser);
        return token;

    }


    @Override
    public boolean isTokenValid(String jwtToken) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwtToken)
                    .getBody();

            return true;
        } catch (Exception e) {
            lof.error("Not a valid token", e);
            return false;
        }

    }

    @Override
    public void expireToken(String appId, String token) {
//        User user = isTokenValid(token);
//        user.setToken("");
//        userManagement.updateUser(appId, user);
    }
}
