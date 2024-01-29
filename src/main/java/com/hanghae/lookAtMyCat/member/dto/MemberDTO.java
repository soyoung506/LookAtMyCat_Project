package com.hanghae.lookAtMyCat.member.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {
    private Long userKey;
    private String userEmail;
    private String userNick;
    private String userPwd;
    private String userName;
    private String userTel;
    private String userIntro;

    private MultipartFile userImage;
    private String originalFileName;  // 원본 파일 이름
    private String storedFileName;  // 서버 저장용 파일 이름

    private boolean userEnable;
    private String userAdmin;
}
