package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "user-secret-key";
    private static final long EXPIRE_TIME = 15 * 60 * 1000;

    public static String generateToken(String id) {
        long nowMillis = System.currentTimeMillis();
        return Jwts.builder()
                .claim("id", id)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(nowMillis + EXPIRE_TIME))
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token.replace("Bearer ", ""));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Authentication getAuthentication(String token) {
        String id = "";
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token.replace("Bearer ", "")).getBody();
            id = claims.get("id", String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UsernamePasswordAuthenticationToken(id, null, null);
    }
}
