package com.hanghae.lookAtMyCat.shopping.handler.exception;

public class ListTypeNotFoundException extends RuntimeException{

    private final String message;

    public ListTypeNotFoundException() {
        this.message = "정렬 방식이 존재하지 않습니다.";
    }

    public ListTypeNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
