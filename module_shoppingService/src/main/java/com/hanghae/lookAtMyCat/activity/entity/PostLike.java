package com.hanghae.lookAtMyCat.activity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postLikeKey;

    // userKey와 join
    @Column(nullable = false)
    private Long userKey;

    @Column(nullable = false, length = 30)
    private String userNick;

    // postKey와 join
    @ManyToOne
    @JoinColumn(nullable = false, name = "postKey")
    private Post post;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime postLikeDate;

    @Builder
    public PostLike(Long userKey, String userNick, Post post) {
        this.userKey = userKey;
        this.userNick = userNick;
        this.post = post;
    }

}
