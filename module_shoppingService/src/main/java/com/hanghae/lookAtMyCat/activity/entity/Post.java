package com.hanghae.lookAtMyCat.activity.entity;

import com.hanghae.lookAtMyCat.activity.util.StringListConverter;
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
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postKey;

    // userKey와 join
    @Column(nullable = false)
    private Long userKey;

    @Column(nullable = false, length = 30)
    private String userNick;

    @Column(nullable = false, length = 2000)
    private String postContent;

    @Convert(converter = StringListConverter.class)
    @Column(nullable = false, length = 2000)
    private List<String> postImage;

    @Column(nullable = false, length = 2000)
    private String postImageDir;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLike;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime postDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime postUpdate;

    @Builder
    public Post (Long userKey, String userNick, String postContent, List<String> postImage, String postImageDir) {
        this.userKey = userKey;
        this.userNick = userNick;
        this.postContent = postContent;
        this.postImage = postImage;
        this.postImageDir = postImageDir;
    }

    // 게시글 수정
    public void update(String postContent) {
        this.postContent = postContent;
    }
}
