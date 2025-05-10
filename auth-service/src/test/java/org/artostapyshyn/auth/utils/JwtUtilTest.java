package org.artostapyshyn.auth.utils;

import io.jsonwebtoken.Claims;
import org.artostapyshyn.auth.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(jwtUtil, "secret", "mysecretkeymysecretkeymysecretkeymyse"); // must be 32+ bytes
        ReflectionTestUtils.setField(jwtUtil, "expiration", "3600");
        jwtUtil.initKey();
    }

    @Test
    void testGenerateAndParseToken() {
        String token = jwtUtil.generateToken("123", "USER", "ACCESS");

        Claims claims = jwtUtil.getClaims(token);

        assertEquals("123", claims.get("id"));
        assertEquals("USER", claims.get("role"));
        assertFalse(jwtUtil.getExpirationDate(token).before(new Date()));
    }
}

