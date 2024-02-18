package com.hanghae.lookAtMyCat.activity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commLikeKey;

    // userKey와 join
    @Column(nullable = false)
    private Long userKey;

    // commKey와 join
    @ManyToOne
    @JoinColumn(nullable = false, name = "commKey")
    private Comment comment;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime commLikeDate;

    @Builder
    public CommentLike(Comment comment, Long userKey) {
        this.comment = comment;
        this.userKey = userKey;
    }

}
