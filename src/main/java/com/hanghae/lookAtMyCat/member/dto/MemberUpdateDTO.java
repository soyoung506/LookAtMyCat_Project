package com.hanghae.lookAtMyCat.member.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberUpdateDTO {

    private String userName;
    private String userTel;
    private String userIntro;
    private MultipartFile userImage;
    private String storedFileName;

    @Builder
    public MemberUpdateDTO(String userName, String userTel, String userIntro, String storedFileName) {
        this.userName = userName;
        this.userTel = userTel;
        this.userIntro = userIntro;
        this.storedFileName = storedFileName;
    }

}
