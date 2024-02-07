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
public class CommentResponseDTO {

    @NotEmpty
    private Long commKey;

    @NotEmpty
    private Long userKey;

    @NotEmpty
    private String userNick;

    @NotEmpty
    private String commContent;

    @NotEmpty
    private Long likeCount;

    @NotEmpty
    private LocalDateTime commDate;

    @NotEmpty
    private LocalDateTime commUpdate;

    private int orderPriority;

}
