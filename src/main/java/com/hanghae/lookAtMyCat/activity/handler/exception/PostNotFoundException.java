package com.hanghae.lookAtMyCat.activity.handler.exception;

public class PostNotFoundException extends RuntimeException{

    private final String message;

    public PostNotFoundException() {
        this.message = "존재하지 않는 포스트입니다.";
    }

    public PostNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
