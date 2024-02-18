package com.hanghae.lookAtMyCat.activity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommentLikeDTO {

    private Long commLikeKey;
    private Long commKey;

}
