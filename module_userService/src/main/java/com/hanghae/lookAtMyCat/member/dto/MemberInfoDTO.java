package com.hanghae.lookAtMyCat.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberInfoDTO {

    private String userEmail;
    private String userNick;
    private String userName;
    private String userTel;
    private String userIntro;
    private String userImage;

    @Builder
    public MemberInfoDTO(String userEmail, String userNick, String userName, String userTel,
                String userIntro, String storedFileName) {
        this.userEmail = userEmail;
        this.userNick = userNick;
        this.userName = userName;
        this.userTel = userTel;
        this.userIntro = userIntro;
        this.userImage = storedFileName;
    }

}
