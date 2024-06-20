package com.minimart.auth.util;

import com.minimart.auth.AuthDetails;
import com.minimart.auth.AuthService;
import com.minimart.common.exception.JWTClaimException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import io.jsonwebtoken.*;

@Component
public class JWTUtil {

    @Autowired
    private AuthService authService;

    private final String secret = "@#$@#$DFGSFSDFSDF";
    private final long expiration = 15 * 60 * 1000; // 15 min
    private final long refExpiration = 5 * 60 * 60 * 1000; // 5 hr

    public String generateToken(AuthDetails details) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleNames = details.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        claims.put("roles", roleNames);
        return this.generateAccessTokens(claims, details.getUsername());
    }

    private String generateAccessTokens(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String generateRefreshToken(String subject) {
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refExpiration))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) throws Exception{
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            throw new JWTClaimException(e.getMessage());
        }
    }
}

