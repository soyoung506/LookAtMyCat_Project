package com.hanghae.lookAtMyCat.member.handler.exception;

public class AccountDisabledException extends RuntimeException{

    private final String message;

    public AccountDisabledException() {
        this.message = "계정 활성화를 위해 이메일을 확인해주세요.";
    }

    public AccountDisabledException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
