package com.hanghae.lookAtMyCat.activity.handler.exception;

public class CommentNotLikedException extends RuntimeException {

    private final String message;

    public CommentNotLikedException() {
        this.message = "좋아요를 등록한 댓글이 아닙니다.";
    }

    public CommentNotLikedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
