package com.hanghae.lookAtMyCat.activity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommentDTO {

    private Long commKey;
    private Long postKey;
    private String commContent;

}
