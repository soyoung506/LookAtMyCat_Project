package com.hanghae.lookAtMyCat.member.handler.exception;

public class SessionExpiredException extends RuntimeException{

    private final String message;

    public SessionExpiredException() {
        this.message = "세션이 만료되었습니다. 다시 로그인 해주세요.";
    }

    public SessionExpiredException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
