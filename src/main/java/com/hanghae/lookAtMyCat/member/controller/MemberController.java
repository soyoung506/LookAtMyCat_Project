package com.hanghae.lookAtMyCat.member.controller;

import com.hanghae.lookAtMyCat.member.dto.MemberDTO;
import com.hanghae.lookAtMyCat.member.dto.MemberLoginDTO;
import com.hanghae.lookAtMyCat.member.service.EmailService;
import com.hanghae.lookAtMyCat.member.service.MemberService;
import com.hanghae.lookAtMyCat.security.dto.MemberLoginResponseDTO;
import com.hanghae.lookAtMyCat.security.dto.RefreshTokenDTO;
import com.hanghae.lookAtMyCat.security.service.JwtTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;
    private final EmailService emailService;
    private final JwtTokenService jwtTokenService;
    private final StringRedisTemplate redisTemplate;

    // 회원가입 및 이메일 인증링크 전송
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@ModelAttribute MemberDTO memberDTO) throws IOException {
        if (memberService.existsByUserEmail(memberDTO.getUserEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
        }

        if (memberService.existsByUserNick(memberDTO.getUserNick())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 닉네임입니다.");
        }

        memberService.joinUser(memberDTO);
        emailService.sendVerificationEmail(memberDTO.getUserEmail());
        return ResponseEntity.ok("회원가입이 완료되었습니다. 인증을 위해 이메일을 확인해 주세요.");
    }

    // 이메일 인증링크 확인
    @GetMapping("/signup/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String userEmail = valueOps.get(token);

        if (userEmail != null) {
            boolean result = memberService.activateUser(userEmail);
            redisTemplate.delete(token);

            if (result) {
                return ResponseEntity.ok("이메일 인증에 성공했습니다.");
            }

        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("기간이 만료되었거나 유효하지 않는 링크입니다.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid MemberLoginDTO loginDTO) {
        Map result = memberService.login(loginDTO);
        String status = (String)result.get("status");

        if(status.equals("login")) {
            MemberDTO memberDTO = (MemberDTO)result.get("memberDTO");
            MemberLoginResponseDTO loginResponseDTO = jwtTokenService.createToken(memberDTO);
            return ResponseEntity.ok(loginResponseDTO);
        } else if (status.equals("Deactivation")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("계정 활성화를 위해 이메일을 확인해주세요.");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("로그인 실패");
    }

    // 로그아웃
    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        jwtTokenService.deleteRefreshToken(refreshTokenDTO.getRefreshToken());
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    // 다른 api 호출 시 access 토큰이 만료되면 해당 api 호출
    @PostMapping("/refreshToken")
    public ResponseEntity requestRefresh(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        MemberLoginResponseDTO loginResponseDTO = jwtTokenService.newAccessToken(refreshTokenDTO.getRefreshToken());
        if (loginResponseDTO == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("세션이 만료되었습니다. 다시 로그인 해주세요.");
        }
        return ResponseEntity.ok(loginResponseDTO);
    }
}
