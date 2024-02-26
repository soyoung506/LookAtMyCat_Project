package com.hanghae.lookAtMyCat.shopping.handler.exception;

public class PurchaseNotFoundException extends RuntimeException{

    private final String message;

    public PurchaseNotFoundException() {
        this.message = "구매내역이 존재하지 않습니다.";
    }

    public PurchaseNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
