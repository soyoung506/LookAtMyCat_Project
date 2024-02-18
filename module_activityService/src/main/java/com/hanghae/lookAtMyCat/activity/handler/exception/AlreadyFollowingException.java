package com.hanghae.lookAtMyCat.activity.handler.exception;

public class AlreadyFollowingException extends RuntimeException{

    private final String message;

    public AlreadyFollowingException() {
        this.message = "이미 팔로우한 회원입니다.";
    }

    public AlreadyFollowingException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
