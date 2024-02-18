package com.hanghae.lookAtMyCat.activity.handler.exception;

public class CommentAlreadyLikedException extends RuntimeException{

    private final String message;

    public CommentAlreadyLikedException() {
        this.message = "이미 좋아요를 등록한 댓글입니다.";
    }

    public CommentAlreadyLikedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
