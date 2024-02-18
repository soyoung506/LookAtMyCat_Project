package com.hanghae.lookAtMyCat.activity.repository;

import com.hanghae.lookAtMyCat.activity.entity.Comment;
import com.hanghae.lookAtMyCat.activity.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentAndUserKey(Comment comment, Long userKey);
}
