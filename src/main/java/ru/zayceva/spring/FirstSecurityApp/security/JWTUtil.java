package ru.zayceva.spring.FirstSecurityApp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String username){
        Date exDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username) // пара ключ-значение
                .withIssuedAt(new Date()) // когда выдан токен
                .withIssuer("spring-app") // кто выдал токен
                .withExpiresAt(exDate) // когда истечет токен
                .sign(Algorithm.HMAC256(secret)); // секретный ключ
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        // только такой токен пройдет валидацию:
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("spring-app")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
