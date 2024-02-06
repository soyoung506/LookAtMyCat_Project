package com.hanghae.lookAtMyCat.activity.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followKey;

    // userKey와 join
    @Column(nullable = false)
    private Long toUserKey;

    // userKey와 join
    @Column(nullable = false)
    private Long fromUserKey;

    @Builder
    public Follow(Long toUserKey, Long fromUserKey) {
        this.toUserKey = toUserKey;
        this.fromUserKey = fromUserKey;
    }
}
