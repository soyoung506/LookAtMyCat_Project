package com.hanghae.lookAtMyCat.activity.repository.customRepository;

import com.hanghae.lookAtMyCat.activity.dto.CommentResponseDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostLikeResponseDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostResponseDTO;
import com.hanghae.lookAtMyCat.activity.entity.*;
import com.hanghae.lookAtMyCat.member.entity.QUser;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CustomRepositoryImpl implements CustomRepository {

    private final JPAQueryFactory query;

    public CustomRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    // 포스트 댓글 리스트 추출
    // 댓글키, 유저키, 유저닉네임, 댓글 내용, 댓글 좋아요수, 댓글 등록일, 댓글 수정일을 좋아요가 많은 순으로 출력
    // 추가요청: 팔로우 유저의 댓글 먼저 출력
    // 서브쿼리 사용해야하냐??
    @Override
    public List<CommentResponseDTO> getCommentList(Post post) {
        QComment comment = QComment.comment;
        QUser user = QUser.user;
        QCommentLike commentLike = QCommentLike.commentLike;
        return query
                .select(Projections.bean(CommentResponseDTO.class, comment.commKey, user.userKey,
                        user.userNick, comment.commContent, commentLike.comment.count().as("likeCount"),
                        comment.commDate, comment.commUpdate))
                .from(comment)
                .join(user)
                .on(comment.userKey.eq(user.userKey))
                .leftJoin(commentLike)
                .on(comment.commKey.eq(commentLike.comment.commKey))
                .groupBy(comment.commKey, user.userNick, comment.commContent)
                .where(comment.post.eq(post))
                .orderBy(commentLike.comment.count().desc())
                .fetch();
    }

    // 포스트 좋아요 리스트 추출
    // 포스트 좋아요키, 유저키, 유저닉네임, 포스트 좋아요 등록일을 최신순으로 출력
    // 추가요청: 팔로우 유저의 좋아요 먼저 출력
    @Override
    public List<PostLikeResponseDTO> getPostLikeList(Post post) {
        QPostLike postLike = QPostLike.postLike;
        QUser user = QUser.user;
        return query
                .select(Projections.bean(PostLikeResponseDTO.class, postLike.postLikeKey, user.userKey,
                        user.userNick, postLike.postLikeDate))
                .from(postLike)
                .join(user)
                .on(postLike.userKey.eq(user.userKey))
                .where(postLike.post.eq(post))
                .orderBy(postLike.postLikeDate.desc())
                .fetch();
    }

    // 포스트 추출
    // 포스트키, 유저키, 유저닉네임, 포스트 내용, 포스트이미지들, 좋아요수, 댓글수, 포스트 등록일, 포스트 수정일 출력
    // 추가요청: 좋아요 한 팔로우 유저의 닉네임(1개), 댓글 단 팔로우 유저의 댓글(1개)
    @Override
    public PostResponseDTO getPost(Long postKey) {
        QPost post = QPost.post;
        QPostLike postLike = QPostLike.postLike;
        QComment comment = QComment.comment;
        QUser user = QUser.user;
        return query
                .select(Projections.bean(PostResponseDTO.class, post.postKey, user.userKey,
                        user.userNick, post.postContent, post.postImage,
                        ExpressionUtils.as(
                                JPAExpressions.select(postLike.postLikeKey.count())
                                        .from(postLike)
                                        .where(postLike.post.postKey.eq(postKey))
                                        ,"likeCount"
                        ),
                        ExpressionUtils.as(
                                JPAExpressions.select(comment.commKey.count())
                                        .from(comment)
                                        .where(comment.post.postKey.eq(postKey))
                                ,"commCount"
                        ),
                        post.postDate, post.postUpdate))
                .from(post)
                .join(user)
                .on(post.userKey.eq(user.userKey))
                .where(post.postKey.eq(postKey))
                .fetchOne();
    }
}
