package com.example.eventmanager.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleException(Exception e) {
//        ErrorMessageResponse exceptionMessageResponse = new ErrorMessageResponse(
//                "Internal error",
//                e.getMessage(),
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(exceptionMessageResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageResponse> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        ErrorMessageResponse messageResponse = new ErrorMessageResponse(
                "Bad request",
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        ErrorMessageResponse messageResponse = new ErrorMessageResponse(
                "Not founded",
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessageResponse> badCredentialsExceptionHandler(BadCredentialsException e) {
        ErrorMessageResponse messageResponse = new ErrorMessageResponse(
                "Bad credential",
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(messageResponse, HttpStatus.UNAUTHORIZED);
    }

}
