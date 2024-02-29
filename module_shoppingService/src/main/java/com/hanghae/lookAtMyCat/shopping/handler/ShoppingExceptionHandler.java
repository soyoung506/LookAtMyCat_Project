package com.hanghae.lookAtMyCat.shopping.handler;

import com.hanghae.lookAtMyCat.shopping.handler.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ShoppingExceptionHandler {

    @ExceptionHandler(value = UserMismatchException.class)
    public ResponseEntity<String> userMismatchException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(value = ListTypeNotFoundException.class)
    public ResponseEntity<String> listTypeNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = ProductNotFoundException.class)
    public ResponseEntity<String> productNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = ProductSoldOutException.class)
    public ResponseEntity<String> productSoldOutException(Exception e) {
        return ResponseEntity.status(HttpStatus.GONE).body(e.getMessage());
    }

    @ExceptionHandler(value = PurchaseNotFoundException.class)
    public ResponseEntity<String> purchaseNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = PurchaseRefusedException.class)
    public ResponseEntity<String> purchaseRefusedException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = CartNotFoundException.class)
    public ResponseEntity<String> cartNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
