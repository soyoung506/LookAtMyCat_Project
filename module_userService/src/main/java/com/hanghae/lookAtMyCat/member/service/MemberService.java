package com.hanghae.lookAtMyCat.member.service;

import com.hanghae.lookAtMyCat.member.dto.*;
import com.hanghae.lookAtMyCat.member.entity.User;
import com.hanghae.lookAtMyCat.member.handler.exception.AccountDisabledException;
import com.hanghae.lookAtMyCat.member.handler.exception.LoginFailedException;
import com.hanghae.lookAtMyCat.member.handler.exception.MemberNotFoundException;
import com.hanghae.lookAtMyCat.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 현재 프로젝트 위치 가져오기: System.getProperty("user.dir")
    private static final String PROFILEPATH =  "D:\\HangHae99\\portfolio_SpringBootProject\\LookAtMyCatPlatform\\module_userService\\images\\userProfiles";

    // 이메일 중복 확인
    @Transactional(readOnly = true)
    public boolean existsByUserEmail(String email) {
        return memberRepository.existsByUserEmail(email);
    }

    // 닉네임 중복 확인
    @Transactional(readOnly = true)
    public boolean existsByUserNick(String nick) {
        return memberRepository.existsByUserNick(nick);
    }

    // 회원가입 시, 회원정보 저장
    @Transactional
    public void joinUser(MemberDTO memberDTO) throws IOException {
        // 1. DTO에 담긴 파일을 꺼내기
        // 2. 파일이름 추출
        // 3. 서버 저장용 이름 생성
        // 4. 저장 경로 설정 (PROFILEPATH)
        // 5. 해당 경로에 파일 저장
        // 6. UserTBL에 데이터 save 처리
        MultipartFile userImage = memberDTO.getUserImage();
        String originalFileName = userImage.getOriginalFilename();
        String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
        userImage.transferTo(new File(PROFILEPATH, storedFileName));

        // 6-1, dto를 entity로 변환
        // 6-2. repository의 save 메소드 호출
        User user = User.builder()
                .userEmail(memberDTO.getUserEmail())
                .userNick(memberDTO.getUserNick())
                .userPwd(passwordEncoder.encode(memberDTO.getUserPwd())) // 암호화하여 저장
                .userName(memberDTO.getUserName())
                .userTel(memberDTO.getUserTel())
                .userIntro(memberDTO.getUserIntro())
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .userEnable(false)  // 회원가입시 이메일인증으로 사용권한부여
                .build();
        memberRepository.save(user);
    }

    // 이메일 인증 성공 시, 계정 활성화
    @Transactional
    public void activateUser(String userEmail) {
        User user = memberRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new MemberNotFoundException("회원정보가 존재하지 않습니다. 회원가입을 진행해 주세요."));
        user.emailVerifiedSuccess();
        memberRepository.save(user);
    }

    // 로그인 성공 여부 확인
    @Transactional
    public MemberDTO login(MemberLoginDTO memberLoginDTO) {
        User user = memberRepository.findByUserEmail(memberLoginDTO.getUserEmail())
                .orElseThrow(LoginFailedException::new); // 회원정보 없음 로그인 실패
        MemberDTO memberDTO = new MemberDTO();
        if(user.getUserEnable()) {
            if (passwordEncoder.matches(memberLoginDTO.getUserPwd(), user.getUserPwd())){
                memberDTO.setUserKey(user.getUserKey());
                memberDTO.setUserEmail(user.getUserEmail());
                memberDTO.setUserNick(user.getUserNick());
                return memberDTO;
            }
            throw new LoginFailedException(); // 비밀번호 미일치 로그인 실패
        }
        throw new AccountDisabledException(); // 계정 비활성화
    }

    // 회원 여부 확인
    @Transactional(readOnly = true)
    public Optional<User> getUser(Long userKey) {
        return memberRepository.findByUserKey(userKey);
    }

    // 회원정보 조회 및 리턴
    @Transactional(readOnly = true)
    public MemberInfoDTO getInfo(Long userKey) {
        User user = memberRepository.findByUserKey(userKey)
                .orElseThrow(MemberNotFoundException::new);
        return MemberInfoDTO.builder()
                .userEmail(user.getUserEmail())
                .userNick(user.getUserNick())
                .userName(user.getUserName())
                .userTel(user.getUserTel())
                .userIntro(user.getUserIntro())
                .storedFileName(user.getStoredFileName())
                .build();
    }
    
    // 회원 프로필 이미지 조회
    @Transactional
    public byte[] getInfoImage(Long userKey) throws IOException{
        User user = memberRepository.findByUserKey(userKey)
                .orElseThrow(MemberNotFoundException::new);
        String filePath = PROFILEPATH + "\\" + user.getStoredFileName();
        return Files.readAllBytes(new File(filePath).toPath());
    }

    // 회원정보 수정
    @Transactional
    public MemberUpdateDTO update(MemberUpdateDTO memberDTO, Long userKey) throws IOException {
        User user = memberRepository.findByUserKey(userKey)
                .orElseThrow(MemberNotFoundException::new);
        MultipartFile userImage = memberDTO.getUserImage();
        String originalFileName = userImage.getOriginalFilename();
        String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
        userImage.transferTo(new File(PROFILEPATH, storedFileName));
        // 기존 프로필 이미지 삭제
        String originalFile = user.getStoredFileName();
        File oldFile = new File(PROFILEPATH + "\\" + originalFile);
        oldFile.delete();

        // 회원정보 일부분 수정
        user.update(memberDTO.getUserName(), memberDTO.getUserTel(), memberDTO.getUserIntro(), originalFileName, storedFileName);
        memberRepository.save(user);
        return MemberUpdateDTO.builder()
                .userName(user.getUserName())
                .userTel(user.getUserTel())
                .userIntro(user.getUserIntro())
                .storedFileName(user.getStoredFileName())
                .build();
    }

    // 비밀번호 수정
    @Transactional
    public void updatePwd(UserPwdDTO userPwdDTO, Long userKey) {
        User user = memberRepository.findByUserKey(userKey)
                .orElseThrow(MemberNotFoundException::new);
        user.updatePwd(passwordEncoder.encode(userPwdDTO.getUserPwd()));
        memberRepository.save(user);
    }

    public String getUserNick(Long userKey) {
        User user = memberRepository.findByUserKey(userKey)
                .orElseThrow(MemberNotFoundException::new);
        return user.getUserNick();
    }
}
