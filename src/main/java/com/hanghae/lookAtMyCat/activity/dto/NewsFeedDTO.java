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
public class NewsFeedDTO {

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

    // 1: 팔로우 사용자의 포스트
    // 2: 팔로우 사용자가 댓글 단 포스트
    // 3: 팔로우 사용자가 좋아요한 포스트
    @NotEmpty
    private int newsFeedType;

    @NotEmpty
    private Long followUserKey;

    @NotEmpty
    private String followUserNick;

}
