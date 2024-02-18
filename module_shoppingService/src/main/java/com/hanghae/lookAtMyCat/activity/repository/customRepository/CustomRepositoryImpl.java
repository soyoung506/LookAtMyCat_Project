package com.hanghae.lookAtMyCat.activity.repository.customRepository;

import com.hanghae.lookAtMyCat.activity.dto.CommentResponseDTO;
import com.hanghae.lookAtMyCat.activity.dto.NewsFeedDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostLikeResponseDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostResponseDTO;
import com.hanghae.lookAtMyCat.activity.entity.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomRepositoryImpl implements CustomRepository {

    private final JPAQueryFactory query;

    public CustomRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    // 포스트 댓글 리스트 추출
    // 댓글키, 유저키, 유저닉네임, 댓글 내용, 댓글 좋아요수, 댓글 등록일, 댓글 수정일을 좋아요가 많은 순으로 출력
    // 출력 순서: 본인 -> 팔로우 유저 -> 좋아요 많은 순 -> 최신순
    // 서브쿼리 사용해야하냐??
    @Override
    public List<CommentResponseDTO> getCommentList(Post post, Long userKey) {
        QComment comment = QComment.comment;
        QFollow follow = QFollow.follow;
        QCommentLike commentLike = QCommentLike.commentLike;
        return query
                .select(Projections.bean(CommentResponseDTO.class, comment.commKey, comment.userKey,
                        comment.userNick, comment.commContent, commentLike.comment.count().as("likeCount"),
                        comment.commDate, comment.commUpdate,
                        Expressions.cases()
                                .when(comment.userKey.eq(userKey))
                                .then(2)
                                .when(JPAExpressions.selectFrom(follow)
                                        .where(follow.fromUserKey.eq(userKey)
                                                .and(follow.toUserKey.eq(comment.userKey)))
                                        .exists())
                                .then(1)
                                .otherwise(0)
                                .as("orderPriority")))
                .from(comment)
                .leftJoin(commentLike)
                .on(comment.commKey.eq(commentLike.comment.commKey))
                .groupBy(comment.commKey, comment.userNick, comment.commContent)
                .where(comment.post.eq(post))
                .orderBy(Expressions.numberPath(Integer.class, "orderPriority").desc(),
                        commentLike.comment.count().desc(), comment.commDate.desc())
                .fetch();
    }

    // 포스트 좋아요 리스트 추출
    // 포스트 좋아요키, 유저키, 유저닉네임, 포스트 좋아요 등록일을 최신순으로 출력
    // 출력 순서: 본인 -> 팔로우 유저 -> 최신순
    @Override
    public List<PostLikeResponseDTO> getPostLikeList(Post post, Long userKey) {
        QPostLike postLike = QPostLike.postLike;
        QFollow follow = QFollow.follow;
        return query
                .select(Projections.bean(PostLikeResponseDTO.class, postLike.postLikeKey, postLike.userKey,
                        postLike.userNick, postLike.postLikeDate,
                        Expressions.cases()
                                .when(postLike.userKey.eq(userKey))
                                .then(2)
                                .when(JPAExpressions.selectFrom(follow)
                                        .where(follow.fromUserKey.eq(userKey)
                                                .and(follow.toUserKey.eq(postLike.userKey)))
                                        .exists())
                                .then(1)
                                .otherwise(0)
                                .as("orderPriority")))
                .from(postLike)
                .where(postLike.post.eq(post))
                .orderBy(Expressions.numberPath(Integer.class, "orderPriority").desc(),
                        postLike.postLikeDate.desc())
                .fetch();
    }

    // 포스트 추출
    // 포스트키, 유저키, 유저닉네임, 포스트 내용, 포스트이미지들, 좋아요수, 댓글수, 포스트 등록일, 포스트 수정일 출력
    // 추가출력: 좋아요 한 팔로우 유저의 닉네임(1개), 댓글 단 팔로우 유저의 댓글(1개)
    @Override
    public PostResponseDTO getPost(Long postKey, Long userKey) {
        QPost post = QPost.post;
        QPostLike postLike = QPostLike.postLike;
        QComment comment = QComment.comment;
        QFollow follow = QFollow.follow;
        // 포스트에 댓글을 단 팔로우 사용자의 닉네임 및 댓글 추출
        Tuple tupleFollowComment = query.select(comment.userNick, comment.commContent)
                .from(follow)
                .join(comment).on(comment.userKey.eq(follow.toUserKey))
                .where(follow.fromUserKey.eq(userKey)
                        .and(comment.post.postKey.eq(postKey)))
                .orderBy(comment.commDate.desc())
                .limit(1)
                .fetchOne();
        // 포스트 좋아요를 한 팔로우 사용자의 닉네임 추출
        String followLikeNick = query.select(postLike.userNick)
                .from(follow)
                .join(postLike).on(postLike.userKey.eq(follow.toUserKey))
                .where(follow.fromUserKey.eq(userKey)
                        .and(postLike.post.postKey.eq(postKey)))
                .orderBy(postLike.postLikeDate.desc())
                .limit(1)
                .fetchOne();
        // 포스트 정보 추출
        PostResponseDTO postResponseDTO = query
                .select(Projections.bean(PostResponseDTO.class, post.postKey, post.userKey,
                        post.userNick, post.postContent, post.postImage, post.postDate, post.postUpdate,
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
                        )))
                .from(post)
                .where(post.postKey.eq(postKey))
                .fetchOne();
        // postResponseDTO에 팔로우 사용자 닉네임 저장
        postResponseDTO.setFollowLikeNick(followLikeNick != null ? followLikeNick : null);
        postResponseDTO.setFollowCommentNick(tupleFollowComment != null ? tupleFollowComment.get(0, String.class) : null);
        postResponseDTO.setFollowComment(tupleFollowComment != null ? tupleFollowComment.get(1, String.class) : null);
        return postResponseDTO;
    }

    @Override
    public List<NewsFeedDTO> getNewsFeed(Long userKey) {
        QPost post = QPost.post;
        QPostLike postLike = QPostLike.postLike;
        QComment comment = QComment.comment;
        QFollow follow = QFollow.follow;
        // 팔로우 사용자 목록 조회
        List<Long> followingUserIds = query
                .select(follow.toUserKey)
                .from(follow)
                .where(follow.fromUserKey.eq(userKey))
                .fetch();
        // 팔로우 사용자가 작성한 포스트 조회
        List<NewsFeedDTO> posts = query
                .select(Projections.bean(NewsFeedDTO.class, post.postKey, post.userKey, post.userNick, post.postContent,
                        post.postImage, post.postDate, post.postUpdate, ExpressionUtils.as(Expressions.constant(1), "newsFeedType"),
                        follow.toUserKey.as("followUserKey"), follow.toUserNick.as("followUserNick"),
                        ExpressionUtils.as(
                                JPAExpressions.select(postLike.count())
                                        .from(postLike)
                                        .where(postLike.post.postKey.eq(post.postKey))
                                ,"likeCount"
                        ),
                        ExpressionUtils.as(
                                JPAExpressions.select(comment.count())
                                        .from(comment)
                                        .where(comment.post.postKey.eq(post.postKey))
                                ,"commCount"
                        )))
                .from(post)
                .join(follow).on(post.userKey.eq(follow.toUserKey))
                .where(post.userKey.in(followingUserIds)
                        .and(post.postDate.after(LocalDateTime.now().minusDays(7))))
                .groupBy(post.postKey, post.userKey, post.userNick, post.postContent, post.postImage, post.postDate,
                        post.postUpdate, follow.toUserKey, follow.toUserNick)
                .orderBy(post.postDate.desc())
                .fetch();
        // 팔로우 사용자가 댓글 단 포스트
        List<NewsFeedDTO> commentPosts = query
                .select(Projections.bean(NewsFeedDTO.class, post.postKey, post.userKey, post.userNick, post.postContent,
                        post.postImage, post.postDate, post.postUpdate, ExpressionUtils.as(Expressions.constant(2), "newsFeedType"),
                        follow.toUserKey.as("followUserKey"), follow.toUserNick.as("followUserNick"),
                        ExpressionUtils.as(
                                JPAExpressions.select(postLike.count())
                                        .from(postLike)
                                        .where(postLike.post.postKey.eq(post.postKey))
                                ,"likeCount"
                        ),
                        ExpressionUtils.as(
                                JPAExpressions.select(comment.count())
                                        .from(comment)
                                        .where(comment.post.postKey.eq(post.postKey))
                                ,"commCount"
                        )))
                .from(post)
                .leftJoin(comment).on(comment.post.postKey.eq(post.postKey))
                .join(follow).on(comment.userKey.eq(follow.toUserKey))
                .where(comment.userKey.in(followingUserIds)
                        .and(post.userKey.ne(userKey))
                        .and(comment.commDate.after(LocalDateTime.now().minusDays(7))))
                .groupBy(post.postKey, post.userKey, post.userNick, post.postContent, post.postImage, post.postDate,
                        post.postUpdate, follow.toUserKey, follow.toUserNick)
                .orderBy(post.postDate.desc())
                .fetch();
        // 팔로우 사용자가 좋아요한 포스트
        List<NewsFeedDTO> likePosts = query
                .select(Projections.bean(NewsFeedDTO.class, post.postKey, post.userKey, post.userNick, post.postContent,
                        post.postImage, post.postDate, post.postUpdate, ExpressionUtils.as(Expressions.constant(3), "newsFeedType"),
                        follow.toUserKey.as("followUserKey"), follow.toUserNick.as("followUserNick"),
                        ExpressionUtils.as(
                                JPAExpressions.select(postLike.count())
                                        .from(postLike)
                                        .where(postLike.post.postKey.eq(post.postKey))
                                ,"likeCount"
                        ),
                        ExpressionUtils.as(
                                JPAExpressions.select(comment.count())
                                        .from(comment)
                                        .where(comment.post.postKey.eq(post.postKey))
                                ,"commCount"
                        )))
                .from(post)
                .leftJoin(postLike).on(postLike.post.postKey.eq(post.postKey))
                .join(follow).on(postLike.userKey.eq(follow.toUserKey))
                .where(postLike.userKey.in(followingUserIds)
                        .and(post.userKey.ne(userKey))
                        .and(postLike.postLikeDate.after(LocalDateTime.now().minusDays(7))))
                .groupBy(post.postKey, post.userKey, post.userNick, post.postContent, post.postImage, post.postDate,
                        post.postUpdate, follow.toUserKey, follow.toUserNick)
                .orderBy(post.postDate.desc())
                .fetch();
        // 리스트 3개 합치기
        return Stream.concat(posts.stream(), Stream.concat(commentPosts.stream(), likePosts.stream()))
                .collect(Collectors.toMap(NewsFeedDTO::getPostKey, tempPost -> tempPost, (post1, post2) -> post1)) // 중복된 post.postKey 제거
                .values()
                .stream()
                .sorted((post1, post2) -> post2.getPostDate().compareTo(post1.getPostDate())) // postDate 기준으로 내림차순 정렬
                .collect(Collectors.toList());
    }
}
