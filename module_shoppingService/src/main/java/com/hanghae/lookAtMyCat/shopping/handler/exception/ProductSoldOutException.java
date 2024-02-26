package com.hanghae.lookAtMyCat.shopping.handler.exception;

public class ProductSoldOutException extends RuntimeException{

    private final String message;

    public ProductSoldOutException() {
        this.message = "품절된 상품입니다.";
    }

    public ProductSoldOutException(String message) {
        this.message = message;
    }

    public ProductSoldOutException(Long prodKey) {
        this.message = "품절된 상품입니다. 상품번호: " + prodKey;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
