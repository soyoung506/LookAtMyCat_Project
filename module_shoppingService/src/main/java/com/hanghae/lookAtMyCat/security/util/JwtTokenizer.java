package com.hanghae.lookAtMyCat.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenizer {
    private final byte[] accessSecret;
    private final byte[] refreshSecret;

    public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L; // 30 minutes
    public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7 days

    public JwtTokenizer(@Value("${spring.jwt.accessKey}") String accessSecret, @Value("${spring.jwt.refreshKey}") String refreshSecret) {
        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
    }

    // AccessToken 생성
    public String createAccessToken(Long userKey, String userEmail) {
        return createToken(userKey, userEmail, ACCESS_TOKEN_EXPIRE_COUNT, accessSecret);
    }

    // RefreshToken 생성
    public String createRefreshToken(Long userKey, String userEmail) {
        return createToken(userKey, userEmail, REFRESH_TOKEN_EXPIRE_COUNT, refreshSecret);
    }

    // JWT 토큰 발행 (access, refresh)
    private String createToken(Long userKey, String userEmail, Long expire, byte[] secretKey) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("userKey", userKey);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expire))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    // 토큰에서 유저 아이디 얻기
    public Long getUserIdFromToken(String token) {
        String[] tokenArr = token.split(" ");
        token = tokenArr[1];
        Claims claims = parseToken(token, accessSecret);
        return Long.valueOf((Integer)claims.get("userKey"));
    }

    // access 토큰으로 Claims 형변환 및 복호화
    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, accessSecret);
    }

    // refresh 토큰으로 Claims 형변환 및 복호화
    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, refreshSecret);
    }

    public Claims parseToken(String token, byte[] secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static Key getSigningKey(byte[] secretKey) {
        return Keys.hmacShaKeyFor(secretKey);
    }
}
