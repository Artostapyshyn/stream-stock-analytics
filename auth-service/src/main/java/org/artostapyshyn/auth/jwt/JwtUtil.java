package org.artostapyshyn.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private Key key;

    @PostConstruct
    public void initKey(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getClaims(String token){
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Date getExpirationDate(String token){
        return getClaims(token).getExpiration();
    }

    private boolean isExpired(String token){
        return getExpirationDate(token).before(new Date());
    }

    public String generateToken(String userId, String role, String tokenType) {
        Map<String, String> claims = Map.of("id", userId, "role", role);
        long expMillis = "ACCESS".equalsIgnoreCase(tokenType)
                ? Long.parseLong(expiration) * 1000
                : Long.parseLong(expiration) * 1000 * 5;

        final Date now = new Date();
        final Date exp = new Date(now.getTime() + expMillis);

        return Jwts.builder()
                .setSubject(claims.get("id"))
                .setClaims(claims)
                .setExpiration(exp)
                .setIssuedAt(now)
                .signWith(key)
                .compact();
    }
}
