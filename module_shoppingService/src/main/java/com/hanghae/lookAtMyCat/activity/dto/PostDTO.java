package com.hanghae.lookAtMyCat.activity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PostDTO {

    private Long postKey;
    private String postContent;
    private List<MultipartFile> postImage;
    private String postImageName;

}
