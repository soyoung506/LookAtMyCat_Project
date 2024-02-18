package com.hanghae.lookAtMyCat.member.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter  // getter 자동 생성
@NoArgsConstructor(access= AccessLevel.PROTECTED)  // 파라미터 없는 생성자 자동 생성
@Entity // DB가 해당 객체를 인식
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto_increment
    private Long userKey;

    @Column(nullable = false, length = 50, unique = true)
    private String userEmail;

    @Column(nullable = false, length = 30, unique = true)
    private String userNick;

    @Column(nullable = false, length = 150)
    private String userPwd;

    @Column(nullable = false, length = 15)
    private String userName;

    @Column(nullable = false, length = 15)
    private String userTel;

    @Column(nullable = false, length = 500)
    private String userIntro;

    @Column(nullable = false, length = 100)
    private String originalFileName;

    @Column(nullable = false, length = 100)
    private String storedFileName;

    @Column(nullable = false)
    private Boolean userEnable;

    @Column(nullable = false, length = 1, columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String userAdmin;

//    private String refreshToken;

    @PrePersist
    public void prePersist() {
        if (userAdmin == null) {
            userAdmin = "N";
        }
    }

    // 회원가입 빌더
    @Builder
    public User(String userEmail, String userNick, String userPwd, String userName, String userTel,
                String userIntro, String originalFileName, String storedFileName, boolean userEnable) {
        this.userEmail = userEmail;
        this.userNick = userNick;
        this.userPwd = userPwd;
        this.userName = userName;
        this.userTel = userTel;
        this.userIntro = userIntro;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.userEnable = userEnable;
    }

    // 팔로우 시 필요한 빌더
    @Builder
    public User(Long userKey) {
        this.userKey = userKey;
    }


    public void emailVerifiedSuccess() {
        // 회원가입 이메일 인증 완료
        this.userEnable = true;
    }

    // 회원 정보 수정 빌더
    public void update(String userName, String userTel, String userIntro, String originalFileName, String storedFileName) {
        this.userName = userName;
        this.userTel = userTel;
        this.userIntro = userIntro;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
    }

    public void updatePwd(String userPwd) {
        this.userPwd = userPwd;
    }

}
