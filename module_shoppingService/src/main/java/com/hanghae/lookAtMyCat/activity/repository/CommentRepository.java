package com.hanghae.lookAtMyCat.activity.repository;

import com.hanghae.lookAtMyCat.activity.entity.Comment;
import com.hanghae.lookAtMyCat.activity.repository.customRepository.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomRepository {

}
