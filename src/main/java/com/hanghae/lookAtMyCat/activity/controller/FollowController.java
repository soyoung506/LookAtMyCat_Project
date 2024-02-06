package com.hanghae.lookAtMyCat.activity.controller;

import com.hanghae.lookAtMyCat.activity.dto.FollowDTO;
import com.hanghae.lookAtMyCat.activity.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PostMapping
    public ResponseEntity<String> follow(@RequestBody FollowDTO followDTO, Authentication authentication) {
        if(Long.parseLong(authentication.getName()) == followDTO.getToUserKey()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 접근입니다. (본인 팔로우 거부)");
        }
        followService.follow(followDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("팔로우 했습니다.");
    }

    // 팔로우 취소
    @DeleteMapping
    public ResponseEntity<String> followCancel(@RequestBody FollowDTO followDTO, Authentication authentication) {
        if(Long.parseLong(authentication.getName()) == followDTO.getToUserKey()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 접근입니다. (본인 팔로우 해제 거부)");
        }
        followService.followCancel(followDTO, Long.parseLong(authentication.getName()));
        return ResponseEntity.ok("팔로우를 해제했습니다.");
    }
}
