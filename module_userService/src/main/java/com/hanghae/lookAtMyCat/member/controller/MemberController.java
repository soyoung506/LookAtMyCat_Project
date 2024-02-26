package com.hanghae.lookAtMyCat.member.controller;

import com.hanghae.lookAtMyCat.member.dto.*;
import com.hanghae.lookAtMyCat.member.service.EmailService;
import com.hanghae.lookAtMyCat.member.service.MemberService;
import com.hanghae.lookAtMyCat.member.service.TokenService;
import com.hanghae.lookAtMyCat.security.dto.MemberLoginResponseDTO;
import com.hanghae.lookAtMyCat.security.dto.RefreshTokenDTO;
import com.hanghae.lookAtMyCat.security.service.JwtTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;
    private final EmailService emailService;
    private final JwtTokenService jwtTokenService;
    private final TokenService tokenService;

    // 회원가입 및 이메일 인증링크 전송
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@ModelAttribute MemberDTO memberDTO) throws IOException {
        if (memberService.existsByUserEmail(memberDTO.getUserEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
        }
        if (memberService.existsByUserNick(memberDTO.getUserNick())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 닉네임입니다.");
        }
        memberService.joinUser(memberDTO);
        emailService.sendVerificationEmail(memberDTO.getUserEmail());
        return ResponseEntity.ok("회원가입이 완료되었습니다. 인증을 위해 이메일을 확인해 주세요.");
    }

    // 이메일 인증링크 확인
    @GetMapping("/signup/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        tokenService.deleteToken(token);
        return ResponseEntity.ok("이메일 인증에 성공했습니다.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponseDTO> login(@RequestBody MemberLoginDTO loginDTO, HttpServletResponse response) {
        MemberDTO memberDTO = memberService.login(loginDTO);
        MemberLoginResponseDTO loginResponseDTO = jwtTokenService.createToken(memberDTO);
        // 쿠키에 HttpOnly로 access 토큰 저장
        jwtTokenService.addTokenToCookie(response, loginResponseDTO.getAccessToken());
        // 쿠키 or 로컬스토리지에 refresh 토큰 저장

        return ResponseEntity.ok(loginResponseDTO);
    }

    // 로그아웃
    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenDTO refreshTokenDTO, HttpServletResponse response) {
        // 레디스에서 refresh 토큰 삭제
        jwtTokenService.deleteRefreshToken(refreshTokenDTO.getRefreshToken());
        // 쿠키에서 access 토큰 삭제
        jwtTokenService.deleteTokenFromCookie(response);
        // 쿠키 or 로컬스토리지에서 refresh 토큰 삭제
        
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    // 다른 api 호출 시 access 토큰이 만료되면 해당 api 호출
    @PostMapping("/refreshToken")
    public ResponseEntity<MemberLoginResponseDTO> requestRefresh(@RequestBody RefreshTokenDTO refreshTokenDTO, HttpServletResponse response) {
        MemberLoginResponseDTO loginResponseDTO = jwtTokenService.newAccessToken(refreshTokenDTO.getRefreshToken());
        jwtTokenService.addTokenToCookie(response, loginResponseDTO.getAccessToken());
        return ResponseEntity.ok(loginResponseDTO);
    }

    // 내 정보 조회
    @GetMapping("/info")
    public ResponseEntity<MemberInfoDTO> info(Authentication authentication) {
        // authentication에 저장된 principal Object 객체를 Long 타입으로 형변환하기
//        Long userKey = Long.valueOf((Integer)authentication.getPrincipal());
//        Long userKey = Long.parseLong(authentication.getName());
        MemberInfoDTO memberInfoDTO = memberService.getInfo(Long.parseLong(authentication.getName()));
        return ResponseEntity.ok(memberInfoDTO);
    }

    // 내 프로필 이미지 조회
    @GetMapping("/infoImage")
    public ResponseEntity<byte[]> infoImage(Authentication authentication) throws IOException{
        byte[] downloadImage = memberService.getInfoImage(Long.parseLong(authentication.getName()));
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadImage);
    }

    // 내 정보 수정
    @PatchMapping("/update")
    public ResponseEntity<MemberUpdateDTO> update(@ModelAttribute MemberUpdateDTO memberUpdateDTO, Authentication authentication) throws IOException {
        MemberUpdateDTO memberDTO = memberService.update(memberUpdateDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok(memberDTO);
    }

    // 비밀번호 수정
    @PatchMapping("/updatePwd")
    public ResponseEntity<String> updatePwd(@RequestBody UserPwdDTO userPwdDTO, Authentication authentication) {
        memberService.updatePwd(userPwdDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    // userKey로 userNick 추출
    @PostMapping("/userNick/{userKey}")
    public String getUserNick(@PathVariable Long userKey) {
        return memberService.getUserNick(userKey);
    }

}
