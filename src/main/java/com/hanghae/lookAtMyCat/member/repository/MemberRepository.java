package com.hanghae.lookAtMyCat.member.repository;

import com.hanghae.lookAtMyCat.member.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<User, Long> {
    boolean existsByUserEmail(String userEmail);
    boolean existsByUserNick(String userNick);

    Optional<User> findByUserEmail(String userEmail);

    void deleteByUserEmail(String userEmail);

    Optional<User> findByUserKey(Long userKey);
}
