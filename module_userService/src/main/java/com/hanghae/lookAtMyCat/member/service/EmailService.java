package com.hanghae.lookAtMyCat.member.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public void sendVerificationEmail(String userEmail) {
        try {
            String verificationToken  = generateVerificationToken();
            String verificationUrl = buildVerificationUrl(verificationToken);
            String redisVerificationToken = "token:verification:" + verificationToken;

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String emailContent = "회원가입을 완료하려면 다음 링크를 클릭하세요: " + verificationUrl;
            helper.setSubject("회원가입 이메일 인증");
            helper.setText(emailContent, true);
            helper.setTo(userEmail);
            helper.setFrom(new InternetAddress("dhtpgns125@naver.com", "Look At My Cat"));

            javaMailSender.send(message);

            redisTemplate.opsForValue().set(redisVerificationToken, userEmail);  // 레디스에 key 토큰, value 이메일 저장
            redisTemplate.expire(redisVerificationToken, 60 * 10, TimeUnit.SECONDS);  // 토큰 만료 시간 10분
        } catch (MailAuthenticationException e) {
            // 메일 서버에 대한 인증 실패
            e.printStackTrace();
            throw new RuntimeException("메일 서버에 인증하는 동안 오류가 발생했습니다.");
        } catch (Exception e) {
            // 기타 메일 발송 관련 오류
            e.printStackTrace();
            throw new RuntimeException("메일 발송 중 오류가 발생했습니다.");
        }
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    public String buildVerificationUrl(String token) {
        // 이메일 인증 링크 생성
        // 해당 URL은 클라이언트가 클릭할 때 호출됨
        String baseUrl = "http://localhost:8081/members/signup/verify";
        return baseUrl + "?token=" + token;
    }

}
