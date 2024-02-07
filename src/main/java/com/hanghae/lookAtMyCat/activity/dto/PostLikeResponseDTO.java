package com.hanghae.lookAtMyCat.activity.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PostLikeResponseDTO {

    // Jackson 규칙에 따라 필드명 수정 or getter명 수정
    @NotEmpty
    private Long postLikeKey;

    @NotEmpty
    private Long userKey;

    @NotEmpty
    private String userNick;

    @NotEmpty
    private LocalDateTime postLikeDate;

    private int orderPriority;

}
