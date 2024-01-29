package com.hanghae.lookAtMyCat.security.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RefreshTokenDTO {

    @NotEmpty
    String refreshToken;
}
