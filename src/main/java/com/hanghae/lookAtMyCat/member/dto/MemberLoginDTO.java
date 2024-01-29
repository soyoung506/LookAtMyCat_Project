package com.hanghae.lookAtMyCat.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginDTO {
    @NotEmpty
    private String userEmail;

    @NotEmpty
    private String userPwd;
}
