package com.hanghae.lookAtMyCat.member.service;

import com.hanghae.lookAtMyCat.member.dto.MemberDTO;
import com.hanghae.lookAtMyCat.member.dto.MemberLoginDTO;
import com.hanghae.lookAtMyCat.member.entity.User;
import com.hanghae.lookAtMyCat.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String PROFILEPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\userProfiles";

    // 이메일 중복 확인
    public boolean existsByUserEmail(String email) {
        return memberRepository.existsByUserEmail(email);
    }

    // 닉네임 중복 확인
    public boolean existsByUserNick(String nick) {
        return memberRepository.existsByUserNick(nick);
    }

    // 회원가입 시, 회원정보 저장
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
    public boolean activateUser(String userEmail) {
        Optional<User> optionalUser = memberRepository.findByUserEmail(userEmail);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.emailVerifiedSuccess();
            memberRepository.save(user);
            return true;
        }

        return false;
    }

    // 로그인 성공 여부 확인
    public Map login(MemberLoginDTO memberLoginDTO) {
        Optional<User> optionalUser = memberRepository.findByUserEmail(memberLoginDTO.getUserEmail());
        Map resultMap = new HashMap<>();
        MemberDTO memberDTO = new MemberDTO();

        // 회원정보 존재할 경우
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 계정이 활성화 되어있을 경우
            if(user.getUserEnable()) {
                // 비밀번호가 일치할 경우
                if (passwordEncoder.matches(memberLoginDTO.getUserPwd(), user.getUserPwd())){
                    memberDTO.setUserKey(user.getUserKey());
                    memberDTO.setUserEmail(user.getUserEmail());
                    memberDTO.setUserNick(user.getUserNick());
                    resultMap.put("status", "login");
                    resultMap.put("memberDTO", memberDTO);
                    return resultMap;
                }
                resultMap.put("status", "fail");
                return resultMap;
            }
            resultMap.put("status", "Deactivation");
            return resultMap;
        }
        resultMap.put("status", "fail");
        return resultMap;
    }

    // 회원정보 조회
    public Optional<User> getUser(Long userKey) {
        return memberRepository.findByUserKey(userKey);
    }
}
