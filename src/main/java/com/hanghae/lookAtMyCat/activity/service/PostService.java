package com.hanghae.lookAtMyCat.activity.service;

import com.hanghae.lookAtMyCat.activity.dto.NewsFeedDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostDTO;
import com.hanghae.lookAtMyCat.activity.dto.PostResponseDTO;
import com.hanghae.lookAtMyCat.activity.entity.Post;
import com.hanghae.lookAtMyCat.activity.handler.exception.PostNotFoundException;
import com.hanghae.lookAtMyCat.activity.handler.exception.UserMismatchException;
import com.hanghae.lookAtMyCat.activity.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private static final String POSTIMAGEPATH =  "D://HangHae99//portfolio_SpringBootProject//LookAtMyCatPlatform//images//postImage//";

    // 게시글 작성
    @Transactional
    public void post(PostDTO postDTO, Long userKey) throws IOException {
        List<MultipartFile> postImage = postDTO.getPostImage();
        String postImageDir = System.currentTimeMillis() + "_" + userKey;
        String postImagePath = POSTIMAGEPATH + postImageDir;
        File postImageFile = new File(postImagePath);
        postImageFile.mkdir();
        List<String> postImageList = new ArrayList<>();

        for (MultipartFile image : postImage) {
            String originalFileName = image.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
            String savePath = postImagePath + "//" + storedFileName;
            image.transferTo(new File(savePath));
            postImageList.add(storedFileName);
        }
        Post post = Post.builder()
                .userKey(userKey)
                .postContent(postDTO.getPostContent())
                .postImage(postImageList)
                .postImageDir(postImageDir)
                .build();
        postRepository.save(post);
    }

    // 게시글 수정
    @Transactional
    public void postUpdate(PostDTO postDTO, Long userKey) {
        Post post = postRepository.findById(postDTO.getPostKey())
                .orElseThrow(PostNotFoundException::new);
        if (!userKey.equals(post.getUserKey())) {
            throw new UserMismatchException();
        }
        post.update(postDTO.getPostContent());
        postRepository.save(post);
    }

    // 게시글 삭제
    @Transactional
    public void postDelete(PostDTO postDTO, Long userKey) {
        Post post = postRepository.findById(postDTO.getPostKey())
                .orElseThrow(PostNotFoundException::new);
        if (!userKey.equals(post.getUserKey())) {
            throw new UserMismatchException();
        }
        String postImageDir = post.getPostImageDir();
        File oldDIr = new File(POSTIMAGEPATH + postImageDir);
        // 폴더 내에 파일들이 있다면 폴더는 삭제되지 않음 -> 파일들을 먼저 삭제한 후 폴더 삭제
        while (oldDIr.exists()) {
            File[] files = oldDIr.listFiles();
            for (File file: files) {
                file.delete();
            }
            if (files.length == 0 && oldDIr.isDirectory()) {
                oldDIr.delete();
            }
        }
        // JPA의 deleteById는 내부에서 findById 조회 후 값이 없을 경우 EmptyResultDataAccessException이 발생한다.
        postRepository.deleteById(postDTO.getPostKey());
    }

    // 게시글 조회
    @Transactional(readOnly = true)
    public PostResponseDTO getPost(PostDTO postDTO, Long userKey) {
        Post post = postRepository.findById(postDTO.getPostKey())
                .orElseThrow(PostNotFoundException::new);
        return postRepository.getPost(post.getPostKey(), userKey);
    }

    // 게시글 이미지 조회
    @Transactional
    public UrlResource getPostImage(PostDTO postDTO) throws IOException {
        Post post = postRepository.findById(postDTO.getPostKey())
                .orElseThrow(PostNotFoundException::new);
        String postImageDir = post.getPostImageDir();
        return new UrlResource("file:///" + POSTIMAGEPATH + postImageDir + "//" + postDTO.getPostImageName());
    }

    public List<NewsFeedDTO> newsFeed(Long userKey) {
        return postRepository.getNewsFeed(userKey);
    }
}
