package com.hanghae.lookAtMyCat.activity.service;

import com.hanghae.lookAtMyCat.activity.dto.CommentDTO;
import com.hanghae.lookAtMyCat.activity.dto.CommentResponseDTO;
import com.hanghae.lookAtMyCat.activity.entity.Comment;
import com.hanghae.lookAtMyCat.activity.entity.Post;
import com.hanghae.lookAtMyCat.activity.handler.exception.CommentNotFoundException;
import com.hanghae.lookAtMyCat.activity.handler.exception.PostNotFoundException;
import com.hanghae.lookAtMyCat.activity.handler.exception.UserMismatchException;
import com.hanghae.lookAtMyCat.activity.repository.CommentRepository;
import com.hanghae.lookAtMyCat.activity.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // 댓글 작성
    @Transactional
    public void comment(CommentDTO commentDTO, Long userKey) {
        Post post = postRepository.findById(commentDTO.getPostKey())
                .orElseThrow(PostNotFoundException::new);
        Comment comment = Comment.builder()
                .userKey(userKey)
                .post(post)
                .commContent(commentDTO.getCommContent())
                .build();
        commentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public void commentUpdate(CommentDTO commentDTO, Long userKey) {
        Comment comment = commentRepository.findById(commentDTO.getCommKey())
                .orElseThrow(CommentNotFoundException::new);
        if (!userKey.equals(comment.getUserKey())) {
            throw new UserMismatchException();
        }
        comment.update(commentDTO.getCommContent());
        commentRepository.save(comment);
    }

    // 댓글 삭제
    @Transactional
    public void commentDelete(CommentDTO commentDTO, Long userKey) {
        Comment comment = commentRepository.findById(commentDTO.getCommKey())
                .orElseThrow(CommentNotFoundException::new);
        if (!userKey.equals(comment.getUserKey())) {
            throw new UserMismatchException();
        }
        // JPA의 deleteById는 내부에서 findById 조회 후 값이 없을 경우 EmptyResultDataAccessException이 발생한다. -> queryDSL, jpql 사용 필요
        commentRepository.deleteById(commentDTO.getCommKey());
    }

    // 포스트 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> commentGet(CommentDTO commentDTO) {
        Post post = postRepository.findById(commentDTO.getPostKey())
                .orElseThrow(PostNotFoundException::new);
        return commentRepository.getCommentList(post);
    }

}
