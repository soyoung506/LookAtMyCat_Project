package com.hanghae.lookAtMyCat.member.service;

import com.hanghae.lookAtMyCat.member.entity.User;
import com.hanghae.lookAtMyCat.member.handler.exception.MemberNotFoundException;
import com.hanghae.lookAtMyCat.member.handler.exception.TokenExpiredException;
import com.hanghae.lookAtMyCat.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final StringRedisTemplate redisTemplate;
    private final MemberRepository memberRepository;

    @Scheduled(fixedRate = 10000) // 10초마다 실행
    public void processExpiredTokens() {
        Set<String> expiredTokens = findExpiredTokens();
        for (String verificationToken : expiredTokens) {
            handleTokenExpired(verificationToken);
        }
    }

    private Set<String> findExpiredTokens() {
        // 패턴에 매치되는 토큰들 가져오기
        Set<String> allTokens = redisTemplate.keys("expired_tokens:*");
        Set<String> expiredTokens = new HashSet<>();

        // 각 토큰에 대해 만료 여부 확인
        for (String verificationToken : Objects.requireNonNull(allTokens)) {
            if (redisTemplate.getExpire(verificationToken, TimeUnit.SECONDS) <= 0) {
                // 만료된 토큰인 경우 추가
                expiredTokens.add(verificationToken);
            }
        }

        return expiredTokens;
    }

    private void handleTokenExpired(String verificationToken) {
        String userEmail = redisTemplate.opsForValue().get(verificationToken);

        if (userEmail != null) {
            // 만료된 토큰에 해당하는 엔터티 삭제
            memberRepository.deleteByUserEmail(userEmail);
            // 레디스에서 토큰 삭제
            redisTemplate.delete(verificationToken);
        }
    }

    // 이메일 인증 시, 계정 활성화 및 이메일 인증 토큰 삭제
    @Transactional
    public void deleteToken(String token) {
        String redisVerificationToken = "token:verification:" + token;
        String userEmail = redisTemplate.opsForValue().get(redisVerificationToken);
        if (userEmail != null) {
            User user = memberRepository.findByUserEmail(userEmail)
                    .orElseThrow(() -> new MemberNotFoundException("회원정보가 존재하지 않습니다. 회원가입을 진행해 주세요."));
            user.emailVerifiedSuccess();
            memberRepository.save(user);
            redisTemplate.unlink(redisVerificationToken);
        }else {
            throw new TokenExpiredException();
        }
    }
}
