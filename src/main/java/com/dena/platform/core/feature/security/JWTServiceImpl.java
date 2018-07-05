package com.dena.platform.core.feature.security;

import com.dena.platform.common.config.DenaConfigReader;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(JWTServiceImpl.class);

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
        claimedUser.setLastValidToken(token);
        userManagement.updateUser(appId, claimedUser);
        return token;

    }


    @Override
    public User validate(String token) {
        User user = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            user = new User();
            String username = body.getSubject();
            String appId = body.get("app_id", String.class);
            user.setEmail(username);

            User loadedUser = userManagement.findUserById(appId, username);
            if (!StringUtils.isEmpty(loadedUser.getLastValidToken()) && loadedUser.getLastValidToken().equals(token))
                return user;
            else
                return null;

        } catch (Exception e) {
            LOGGER.error("not a valid token", e);
        }

        return user;
    }

    @Override
    public void expireToken(String appId, String token) {
        User user = validate(token);
        user.setLastValidToken("");
        userManagement.updateUser(appId, user);
    }
}
