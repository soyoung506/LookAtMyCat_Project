package com.hanghae.lookAtMyCat.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserPwdDTO {

    @NotEmpty
    private String userPwd;

}
