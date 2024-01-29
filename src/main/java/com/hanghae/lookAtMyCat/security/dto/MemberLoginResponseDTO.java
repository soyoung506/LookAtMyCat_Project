package com.hanghae.lookAtMyCat.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDTO {
    private String accessToken;
    private String refreshToken;

    private Long userKey;
    private String userNick;
}