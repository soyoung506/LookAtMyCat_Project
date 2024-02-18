package com.hanghae.lookAtMyCat.activity.repository.customRepository;


import com.hanghae.lookAtMyCat.activity.dto.CommentResponseDTO;
import com.hanghae.lookAtMyCat.activity.dto.NewsFeedDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostLikeResponseDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostResponseDTO;
import com.hanghae.lookAtMyCat.activity.entity.Post;

import java.util.List;

public interface CustomRepository {

    List<CommentResponseDTO> getCommentList(Post post, Long userKey);

    List<PostLikeResponseDTO> getPostLikeList(Post post, Long userKey);

    PostResponseDTO getPost(Long postKey, Long userKey);

    List<NewsFeedDTO> getNewsFeed(Long userKey);
}
