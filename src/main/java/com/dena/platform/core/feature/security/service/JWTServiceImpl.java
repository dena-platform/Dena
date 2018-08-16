package com.dena.platform.core.feature.security.service;

import com.dena.platform.common.config.DenaConfigReader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Date;

/**
 * @author Nazarpour.
 */
@Service("jwtService")
public class JWTServiceImpl implements JWTService {
    private final static Logger lof = LoggerFactory.getLogger(JWTServiceImpl.class);


    private int tokenExpireDuration;

    @PostConstruct
    private void init() {
        tokenExpireDuration = DenaConfigReader.readIntProperty("dena.security.jwt.token.expire.duration.millis",
                1_800_000);
    }

    @Override
    public String generateJWTToken(String appId, String userName, final String secret) {

        Date expireDate = Date.from(Instant.now().plusMillis(tokenExpireDuration));

        Claims claims = Jwts.claims()
                .setSubject(userName)
                .setExpiration(expireDate);

        claims.put(ROLE, "fixed_role"); //TODO change role to user role
        claims.put(APP_ID, appId);
        claims.put(USER_NAME, userName);
        claims.put(CREATION_DATE, Instant.now());


        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return token;

    }


    @Override
    public boolean isTokenValid(String jwtToken, final String secret) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwtToken)
                    .getBody();

            return true;
        } catch (Exception e) {
            lof.trace("Not a valid token", e);
            return false;
        }

    }

}
