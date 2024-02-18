package com.hanghae.lookAtMyCat.activity.service;

import com.hanghae.lookAtMyCat.activity.client.UserClient;
import com.hanghae.lookAtMyCat.activity.dto.FollowDTO;
import com.hanghae.lookAtMyCat.activity.entity.Follow;
import com.hanghae.lookAtMyCat.activity.handler.exception.AlreadyFollowingException;
import com.hanghae.lookAtMyCat.activity.handler.exception.NotFollowingException;
import com.hanghae.lookAtMyCat.activity.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserClient userClient;

    // 팔로우
    @Transactional
    public void follow(FollowDTO followDTO, Long userKey) {
        String toUserNick = userClient.getUserNickByUserKey(followDTO.getToUserKey());
        followRepository.findByToUserKeyAndFromUserKey(followDTO.getToUserKey(), userKey)
                .ifPresent(value -> { throw new AlreadyFollowingException(); });
        Follow follow = new Follow(followDTO.getToUserKey(), toUserNick, userKey);
        followRepository.save(follow);
    }

    // 팔로우 취소
    @Transactional
    public void followCancel(FollowDTO followDTO, Long userKey) {
        Follow follow = followRepository.findByToUserKeyAndFromUserKey(followDTO.getToUserKey(), userKey)
                .orElseThrow(NotFollowingException::new);
        followRepository.delete(follow);
    }
}
