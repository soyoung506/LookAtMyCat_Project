package com.hanghae.lookAtMyCat.activity.repository;

import com.hanghae.lookAtMyCat.activity.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

//    @Modifying
//    @Transactional
//    @Query(value = "select * from Follow f where f.toUserKey = :toUserKey and f.fromUserKey = :fromUserKey", nativeQuery = true)
//    Optional<Follow> findByToUserKeyAndFromUserKey(@Param("toUserKey")Long toUserKey, @Param("fromUserKey") Long fromUserKey);
    Optional<Follow> findByToUserKeyAndFromUserKey(Long toUserKey, Long fromUserKey);
}
