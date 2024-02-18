package com.hanghae.lookAtMyCat.activity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PostLikeDTO {

    // Jackson 규칙에 따라 필드명 수정 or getter명 수정
    private Long postLikeKey;
    private Long postKey;

}
