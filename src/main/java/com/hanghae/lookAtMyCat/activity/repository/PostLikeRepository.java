package com.hanghae.lookAtMyCat.activity.repository;

import com.hanghae.lookAtMyCat.activity.entity.Post;
import com.hanghae.lookAtMyCat.activity.entity.PostLike;
import com.hanghae.lookAtMyCat.activity.repository.customRepository.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>, CustomRepository {

    Optional<PostLike> findByPostAndUserKey(Post post, Long userKey);

}
