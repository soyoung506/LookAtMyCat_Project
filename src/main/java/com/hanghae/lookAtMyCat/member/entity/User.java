package com.hanghae.lookAtMyCat.member.entity;

import com.hanghae.lookAtMyCat.member.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter  // getter 자동 생성
@Setter  // setter 자동 생성
@NoArgsConstructor(access= AccessLevel.PROTECTED)  // 파라미터 없는 생성자 자동 생성
@Table(name="userTBL")  // 테이블명 설정
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

    public void emailVerifiedSuccess() {
        this.userEnable = true;
    }
}
