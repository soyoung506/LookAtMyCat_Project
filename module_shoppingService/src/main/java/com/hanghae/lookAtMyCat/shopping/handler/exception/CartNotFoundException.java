package com.hanghae.lookAtMyCat.shopping.handler.exception;

public class CartNotFoundException extends RuntimeException{

    private final String message;

    public CartNotFoundException() {
        this.message = "장바구니에 해당 상품이 존재하지 않습니다.";
    }

    public CartNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
