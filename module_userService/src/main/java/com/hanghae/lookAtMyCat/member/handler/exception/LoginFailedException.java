package com.hanghae.lookAtMyCat.member.handler.exception;

public class LoginFailedException extends RuntimeException{

    private final String message;

    public LoginFailedException() {
        this.message = "로그인에 실패했습니다.";
    }

    public LoginFailedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
