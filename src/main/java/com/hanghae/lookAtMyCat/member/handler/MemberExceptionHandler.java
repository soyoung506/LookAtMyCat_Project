package com.hanghae.lookAtMyCat.member.handler;

import com.hanghae.lookAtMyCat.activity.handler.exception.*;
import com.hanghae.lookAtMyCat.member.handler.exception.AccountDisabledException;
import com.hanghae.lookAtMyCat.member.handler.exception.LoginFailedException;
import com.hanghae.lookAtMyCat.member.handler.exception.MemberNotFoundException;
import com.hanghae.lookAtMyCat.member.handler.exception.SessionExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(value = UserMismatchException.class)
    public ResponseEntity<String> userMismatchException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(value = MemberNotFoundException.class)
    public ResponseEntity<String> memberNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = LoginFailedException.class)
    public ResponseEntity<String> loginFailedException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = AccountDisabledException.class)
    public ResponseEntity<String> accountDisabledException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = SessionExpiredException.class)
    public ResponseEntity<String> sessionExpiredException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
