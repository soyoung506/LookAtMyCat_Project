package com.hanghae.lookAtMyCat.activity.handler.exception;

public class PostAlreadyLikedException extends RuntimeException{

    private final String message;

    public PostAlreadyLikedException() {
        this.message = "이미 좋아요를 등록한 포스트입니다.";
    }

    public PostAlreadyLikedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
