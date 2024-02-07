package com.hanghae.lookAtMyCat.activity.service;

import com.hanghae.lookAtMyCat.activity.dto.CommentLikeDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostLikeDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostLikeResponseDTO;
import com.hanghae.lookAtMyCat.activity.entity.Comment;
import com.hanghae.lookAtMyCat.activity.entity.CommentLike;
import com.hanghae.lookAtMyCat.activity.entity.Post;
import com.hanghae.lookAtMyCat.activity.entity.PostLike;
import com.hanghae.lookAtMyCat.activity.handler.exception.*;
import com.hanghae.lookAtMyCat.activity.repository.CommentLikeRepository;
import com.hanghae.lookAtMyCat.activity.repository.CommentRepository;
import com.hanghae.lookAtMyCat.activity.repository.PostLikeRepository;
import com.hanghae.lookAtMyCat.activity.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 게시글 좋아요
    @Transactional
    public void postLike(PostLikeDTO postLikeDTO, Long userKey) {
        Post post = postRepository.findById(postLikeDTO.getPostKey())
                .orElseThrow(PostNotFoundException::new);  // 포스트가 존재하지 않는다면 예외처리
        postLikeRepository.findByPostAndUserKey(post, userKey)
                .ifPresent(value -> { throw new PostAlreadyLikedException(); });  // 이미 좋아요를 누른 포스트라면 예외처리
        PostLike postLike = PostLike.builder()
                .userKey(userKey)
                .post(post)
                .build();
        postLikeRepository.save(postLike);
    }

    // 게시글 좋아요 취소
    @Transactional
    public void postLikeCancel(PostLikeDTO postLikeDTO, Long userKey) {
        Post post = postRepository.findById(postLikeDTO.getPostKey())
                .orElseThrow(PostNotFoundException::new);  // 포스트가 존재하지 않는다면 예외처리
        PostLike postLike = postLikeRepository.findByPostAndUserKey(post, userKey)
                .orElseThrow(PostNotLikedException::new);  // 좋아요를 누른 적이 없다면 예외처리
        postLikeRepository.delete(postLike);
    }
    
    // 게시글 좋아요 조회
    @Transactional(readOnly = true)
    public List<PostLikeResponseDTO> postLikeGet(PostLikeDTO postLikeDTO, Long userKey) {
        Post post = postRepository.findById(postLikeDTO.getPostKey())
                .orElseThrow(PostNotFoundException::new);  // 포스트가 존재하지 않는다면 예외처리
        return postLikeRepository.getPostLikeList(post, userKey);
    }
    

    // 게시글 좋아요와 동일한 루트
    // 댓글 좋아요
    @Transactional
    public void commentLike(CommentLikeDTO commentLikeDTO, Long userKey) {
        // Post와 join으로 함께 조회됨 (쿼리문 작성 요함)
        Comment comment = commentRepository.findById(commentLikeDTO.getCommKey())
                .orElseThrow(CommentNotFoundException::new);
        commentLikeRepository.findByCommentAndUserKey(comment, userKey)
                .ifPresent(value -> { throw new CommentAlreadyLikedException(); });
        CommentLike commentLike = CommentLike.builder()
                .userKey(userKey)
                .comment(comment)
                .build();
        commentLikeRepository.save(commentLike);
    }

    // 댓글 좋아요 취소
    @Transactional
    public void commentLikeCancel(CommentLikeDTO commentLikeDTO, Long userKey) {
        Comment comment = commentRepository.findById(commentLikeDTO.getCommKey())
                .orElseThrow(CommentNotFoundException::new);
        CommentLike commentLike = commentLikeRepository.findByCommentAndUserKey(comment, userKey)
                .orElseThrow(CommentNotLikedException::new);
        commentLikeRepository.delete(commentLike);
    }

}
