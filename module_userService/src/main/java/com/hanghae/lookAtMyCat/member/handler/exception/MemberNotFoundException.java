package com.hanghae.lookAtMyCat.member.handler.exception;

public class MemberNotFoundException extends RuntimeException{

    private final String message;

    public MemberNotFoundException() {
        this.message = "존재하는 회원이 아닙니다.";
    }

    public MemberNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
