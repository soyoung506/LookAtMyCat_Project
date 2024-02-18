package com.hanghae.lookAtMyCat.member.service;

import com.hanghae.lookAtMyCat.member.entity.User;
import com.hanghae.lookAtMyCat.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    private final StringRedisTemplate redisTemplate;
    private final MemberRepository memberRepository;

    public TokenService(StringRedisTemplate redisTemplate, MemberRepository memberRepository) {
        this.redisTemplate = redisTemplate;
        this.memberRepository = memberRepository;
    }

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

}
