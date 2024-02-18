package com.hanghae.lookAtMyCat.activity.handler;

import com.hanghae.lookAtMyCat.activity.handler.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ActivityExceptionHandler {

    @ExceptionHandler(value = UserMismatchException.class)
    public ResponseEntity<String> userMismatchException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(value = PostNotFoundException.class)
    public ResponseEntity<String> postNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = PostAlreadyLikedException.class)
    public ResponseEntity<String> postAlreadyLikedException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = PostNotLikedException.class)
    public ResponseEntity<String> postNotLikedException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = CommentNotFoundException.class)
    public ResponseEntity<String> commentNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = CommentAlreadyLikedException.class)
    public ResponseEntity<String> commentAlreadyLikedException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = CommentNotLikedException.class)
    public ResponseEntity<String> commentNotLikedException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = AlreadyFollowingException.class)
    public ResponseEntity<String> alreadyFollowingException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = NotFollowingException.class)
    public ResponseEntity<String> notFollowingException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
