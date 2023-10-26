package com.craft.craftapp.auth;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final String jwtSecret = "vishD3JK56m6wTTgsNFhqzjqP";

    public boolean validate(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;

    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        log.info("claims {}", claims.toString());
        return claims.get("username", String.class);
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
                .claim("username", user.getUsername())
                .claim("claims", user.getAuthorities())//password not needed, authorities needed.
                .setSubject("finhub")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(2 * 365, ChronoUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        log.debug("created jwt token {} for username {}", jwtToken, user.getUsername());

        return jwtToken;
    }
}
