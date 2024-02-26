package com.hanghae.lookAtMyCat.shopping.handler.exception;

public class PurchaseRefusedException extends RuntimeException{

    private final String message;

    public PurchaseRefusedException() {
        this.message = "상품이 품절되었거나 구매 수량을 초과했습니다.";
    }

    public PurchaseRefusedException(String message) {
        this.message = message;
    }

    public PurchaseRefusedException(Long prodKey) {
        this.message = "상품이 품절되었거나 구매 수량을 초과했습니다. 상품번호: " + prodKey;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
