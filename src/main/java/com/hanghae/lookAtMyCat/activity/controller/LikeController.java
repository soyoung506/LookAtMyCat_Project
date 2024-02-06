package com.hanghae.lookAtMyCat.activity.controller;

import com.hanghae.lookAtMyCat.activity.dto.CommentLikeDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostLikeDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostLikeResponseDTO;
import com.hanghae.lookAtMyCat.activity.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    // 게시글 좋아요
    @PostMapping("/post")
    public ResponseEntity<String> postLike(@RequestBody PostLikeDTO postLikeDTO, Authentication authentication) {
        likeService.postLike(postLikeDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("좋아요가 등록되었습니다.");
    }

    // 게시글 좋아요 취소
    @DeleteMapping("/post")
    public ResponseEntity<String> postLikeCancel(@RequestBody PostLikeDTO postLikeDTO, Authentication authentication) {
        likeService.postLikeCancel(postLikeDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("좋아요를 취소했습니다.");
    }

    // 게시글 좋아요 조회
    @GetMapping("/post")
    public ResponseEntity<List<PostLikeResponseDTO>> postLikeGet(@RequestBody PostLikeDTO postLikeDTO) {
        List<PostLikeResponseDTO> postLikeResponseDTO = likeService.postLikeGet(postLikeDTO);
        return ResponseEntity.ok(postLikeResponseDTO);
    }

    // 댓글 좋아요
    @PostMapping("/comment")
    public ResponseEntity<String> commentLike(@RequestBody CommentLikeDTO commentLikeDTO, Authentication authentication) {
        likeService.commentLike(commentLikeDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("좋아요가 등록되었습니다.");
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/comment")
    public ResponseEntity<String> commentLikeCancel(@RequestBody CommentLikeDTO commentLikeDTO, Authentication authentication) {
        likeService.commentLikeCancel(commentLikeDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("좋아요를 취소했습니다.");
    }
}
