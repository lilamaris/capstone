package com.lilamaris.capstone.adapter.in.web.config;

import com.lilamaris.capstone.adapter.in.web.response.ExceptionResponse;
import com.lilamaris.capstone.application.exception.ApplicationInvariantException;
import com.lilamaris.capstone.application.exception.ConflictException;
import com.lilamaris.capstone.application.exception.DomainViolationException;
import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    @ExceptionHandler(DomainViolationException.class)
    public ResponseEntity<?> handleDomainViolationException(DomainViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.buildFromInternalException(e));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.buildFromInternalException(e));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.buildFromInternalException(e));
    }

    @ExceptionHandler(ApplicationInvariantException.class)
    public ResponseEntity<?> handleInvariantException(ApplicationInvariantException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse.buildFromInternalException(e));
    }
}
