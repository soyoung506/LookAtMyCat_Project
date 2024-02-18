package com.hanghae.lookAtMyCat.activity.handler.exception;

public class PostNotLikedException extends RuntimeException {

    private final String message;

    public PostNotLikedException() {
        this.message = "좋아요를 등록한 포스트가 아닙니다.";
    }

    public PostNotLikedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
