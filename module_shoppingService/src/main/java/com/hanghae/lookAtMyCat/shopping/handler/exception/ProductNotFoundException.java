package com.hanghae.lookAtMyCat.shopping.handler.exception;

public class ProductNotFoundException extends RuntimeException{

    private final String message;

    public ProductNotFoundException() {
        this.message = "상품이 존재하지 않습니다.";
    }

    public ProductNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
