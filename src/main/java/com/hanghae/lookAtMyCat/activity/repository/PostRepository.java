package com.hanghae.lookAtMyCat.activity.repository;

import com.hanghae.lookAtMyCat.activity.entity.Post;
import com.hanghae.lookAtMyCat.activity.repository.customRepository.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, CustomRepository {

}
