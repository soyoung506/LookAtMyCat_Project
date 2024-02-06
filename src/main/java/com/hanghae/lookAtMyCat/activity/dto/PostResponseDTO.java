package com.hanghae.lookAtMyCat.activity.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PostResponseDTO {

    @NotEmpty
    private Long postKey;

    @NotEmpty
    private Long userKey;

    @NotEmpty
    private String userNick;

    @NotEmpty
    private String postContent;

    @NotEmpty
    private List<String> postImage;

    @NotEmpty
    private Long likeCount;

    @NotEmpty
    private Long commCount;

    @NotEmpty
    private LocalDateTime postDate;

    @NotEmpty
    private LocalDateTime postUpdate;

//    private String followUserLike;

//    private List<Comment> commentList;
}
