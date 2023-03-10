package ru.netology.cloudstorage.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import ru.netology.cloudstorage.model.User;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
@Setter
public class TokenUtil {

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Value("${jwt.token.lifetime-in-minutes}")
    private long lifetime;

    public String generateToken(User user) {
        final var subject = String.valueOf(user.getId());
        Instant now = Instant.now();
        Instant exp = now.plus(lifetime, ChronoUnit.MINUTES);
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(getAlgorithm());
    }

    public boolean checkToken(String token) {
        try {
            JWT.require(getAlgorithm()).build().verify(token);
        } catch (JWTVerificationException ex) {
            log.info("The access token is invalid.");
            return false;
        }
        return true;
    }

    public Long getUserIdFromToken(String token) {
        final var jwt = JWT.require(getAlgorithm()).build().verify(token);
        return Long.valueOf(jwt.getSubject());
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secretKey);
    }
}