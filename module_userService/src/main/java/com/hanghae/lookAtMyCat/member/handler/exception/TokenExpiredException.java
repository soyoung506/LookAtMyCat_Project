package com.hanghae.lookAtMyCat.member.handler.exception;

public class TokenExpiredException extends RuntimeException{

    private final String message;

    public TokenExpiredException() {
        this.message = "기간이 만료되었거나 유효하지 않는 링크입니다.";
    }

    public TokenExpiredException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
