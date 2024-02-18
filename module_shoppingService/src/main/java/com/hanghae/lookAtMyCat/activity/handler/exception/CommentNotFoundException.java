package com.hanghae.lookAtMyCat.activity.handler.exception;

public class CommentNotFoundException extends RuntimeException{

    private final String message;

    public CommentNotFoundException() {
        this.message = "존재하지 않는 댓글입니다.";
    }

    public CommentNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
