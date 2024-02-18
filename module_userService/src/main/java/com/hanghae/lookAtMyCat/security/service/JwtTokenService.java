package com.hanghae.lookAtMyCat.security.service;

import com.hanghae.lookAtMyCat.member.dto.MemberDTO;
import com.hanghae.lookAtMyCat.member.entity.User;
import com.hanghae.lookAtMyCat.member.handler.exception.SessionExpiredException;
import com.hanghae.lookAtMyCat.member.service.MemberService;
import com.hanghae.lookAtMyCat.security.dto.MemberLoginResponseDTO;
import com.hanghae.lookAtMyCat.security.util.JwtTokenizer;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtTokenizer jwtTokenizer;
    private final StringRedisTemplate redisTemplate;
    private final MemberService memberService;

    // 로그인 시 access 토큰 및 refresh 토큰 발행
    public MemberLoginResponseDTO createToken(MemberDTO memberDTO) {
        // access, refresh 토큰 발행
        String accessToken = jwtTokenizer.createAccessToken(memberDTO.getUserKey(), memberDTO.getUserEmail());
        String refreshToken = jwtTokenizer.createRefreshToken(memberDTO.getUserKey(), memberDTO.getUserEmail());

        // redis에 refresh 토큰 저장
        redisTemplate.opsForValue().set(refreshToken, String.valueOf(memberDTO.getUserKey()));
        redisTemplate.expire(refreshToken, 7 * 24 * 60 * 60 * 1000L, TimeUnit.MILLISECONDS);  // 토큰 만료 시간 7일

        return MemberLoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userKey(memberDTO.getUserKey())
                .userNick(memberDTO.getUserNick())
                .build();
    }

    // 로그아웃 시 refresh 토큰 삭제
    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.unlink(refreshToken);
    }

    // acceess 토큰 만료 시 refresh 토큰 비교 후 새로운 access 토큰 발행
    public MemberLoginResponseDTO newAccessToken(String refreshToken) {
        String value = redisTemplate.opsForValue().get(refreshToken);
        if (value == null) {
            throw new SessionExpiredException();
        }

        // refresh 토큰 복호화
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
        Long userKey = Long.valueOf((Integer)claims.get("userKey"));

        // 회원탈퇴 여부 확인
        User user = memberService.getUser(userKey).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        String userEmail = claims.getSubject();
        String accessToken = jwtTokenizer.createAccessToken(userKey, userEmail);

        return MemberLoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userKey(userKey)
                .userNick(user.getUserNick())
                .build();
    }

    // 쿠키에 access 토큰 저장
    public void addTokenToCookie(HttpServletResponse response, String accessToken) {
        String encodedToken = URLEncoder.encode("Bearer%" + accessToken, StandardCharsets.UTF_8);
        Cookie cookie = new Cookie("Authorization", encodedToken);
        cookie.setPath("/");
        cookie.setMaxAge(1800);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    // 쿠키에서 access 토큰 삭제
    public void deleteTokenFromCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
