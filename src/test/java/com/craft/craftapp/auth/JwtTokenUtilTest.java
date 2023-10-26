package com.craft.craftapp.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
    }


    @Test
    public void testValidateToken() {
        User user = new User(1L, "user1", "password");

        String token = jwtTokenUtil.generateToken(user);

        assertTrue(jwtTokenUtil.validate(token));
    }

    @Test
    public void testValidateTokenInvalid() {
        String token = "invalid_token";

        assertFalse(jwtTokenUtil.validate(token));
    }
}