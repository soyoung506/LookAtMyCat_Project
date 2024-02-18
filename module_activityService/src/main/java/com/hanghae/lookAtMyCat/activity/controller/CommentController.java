package com.hanghae.lookAtMyCat.activity.controller;

import com.hanghae.lookAtMyCat.activity.dto.CommentDTO;
import com.hanghae.lookAtMyCat.activity.dto.CommentResponseDTO;
import com.hanghae.lookAtMyCat.activity.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<String> comment(@RequestBody CommentDTO commentDTO, Authentication authentication) {
        commentService.comment(commentDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("댓글이 등록 되었습니다.");
    }

    // 댓글 수정
    @PatchMapping
    public ResponseEntity<String> commentUpdate(@RequestBody CommentDTO commentDTO, Authentication authentication) {
        commentService.commentUpdate(commentDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("댓글이 수정되었습니다.");
    }

    // 댓글 삭제
    @DeleteMapping
    public ResponseEntity<String> commentDelete(@RequestBody CommentDTO commentDTO, Authentication authentication) {
        commentService.commentDelete(commentDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    // 포스트 댓글 조회
    // 팔로우 사용자순 -> 좋아요 많은 순 -> 최신순
    @GetMapping
    public ResponseEntity commentGet(@RequestBody CommentDTO commentDTO, Authentication authentication) {
        List<CommentResponseDTO> commentResponseDTO = commentService.commentGet(commentDTO, Long.parseLong(authentication.getName()));
        if(!commentResponseDTO.isEmpty()) {
            return ResponseEntity.ok(commentResponseDTO);
        }
        return ResponseEntity.ok("댓글이 없습니다.");
    }
}
