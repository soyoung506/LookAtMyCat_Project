package com.hanghae.lookAtMyCat.activity.handler.exception;

public class UserMismatchException extends RuntimeException{

    private final String message;

    public UserMismatchException() {
        this.message = "사용자가 일치하지 않습니다.";
    }

    public UserMismatchException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
