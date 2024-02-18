package com.hanghae.lookAtMyCat.activity.handler.exception;

public class NotFollowingException extends RuntimeException{

    private final String message;

    public NotFollowingException() {
        this.message = "팔로우한 회원이 아닙니다.";
    }

    public NotFollowingException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
