package com.hanghae.lookAtMyCat.activity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commKey;

    // userKey와 join
    @Column(nullable = false)
    private Long userKey;

    // postKey와 join
    @ManyToOne
    @JoinColumn(nullable = false, name = "postKey")
    private Post post;

    @Column(nullable = false, length = 1000)
    private String commContent;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> commentLike;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime commDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime commUpdate;

    @Builder
    public Comment(Long userKey, Post post, String commContent) {
        this.userKey = userKey;
        this.post = post;
        this.commContent = commContent;
    }

    public void update(String commContent) {
        this.commContent = commContent;
    }
}
