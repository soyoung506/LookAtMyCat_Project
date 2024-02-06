package com.hanghae.lookAtMyCat.activity.controller;

import com.hanghae.lookAtMyCat.activity.dto.PostDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostResponseDTO;
import com.hanghae.lookAtMyCat.activity.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<String> post(@ModelAttribute PostDTO postDTO, Authentication authentication) throws IOException {
        postService.post(postDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("포스팅 되었습니다.");
    }

    // 게시글 조회
    @GetMapping
    public ResponseEntity<PostResponseDTO> postGet(@RequestBody PostDTO postDTO) {
        PostResponseDTO postResponseDTO = postService.getPost(postDTO);
        return ResponseEntity.ok(postResponseDTO);
    }

    // 게시글 이미지 조회
    @GetMapping("/images")
    public ResponseEntity<UrlResource> postImageGet(@RequestBody PostDTO postDTO) throws IOException {
        UrlResource resource = postService.getPostImage(postDTO);
        String encodedPostImage = UriUtils.encode(postDTO.getPostImageName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedPostImage + "\"";
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType("image/png"))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }


    // 게시글 수정
    @PatchMapping
    public ResponseEntity<String> postUpdate(@RequestBody PostDTO postDTO, Authentication authentication) {
        postService.postUpdate(postDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("포스트가 수정되었습니다.");
    }

    // 게시글 삭제
    @DeleteMapping
    public ResponseEntity<String> postDelete(@RequestBody PostDTO postDTO, Authentication authentication) {
        postService.postDelete(postDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("포스트가 삭제되었습니다.");
    }
}
