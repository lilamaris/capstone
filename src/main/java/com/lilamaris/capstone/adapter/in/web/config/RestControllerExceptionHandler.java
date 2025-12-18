package com.lilamaris.capstone.adapter.in.web.config;

import com.lilamaris.capstone.adapter.in.web.response.ExceptionResponse;
import com.lilamaris.capstone.application.exception.*;
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

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.buildFromInternalException(e));
    }

    @ExceptionHandler(ApplicationInvariantException.class)
    public ResponseEntity<?> handleInvariantException(ApplicationInvariantException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse.buildFromInternalException(e));
    }

    @ExceptionHandler(InfrastructureFailureException.class)
    public ResponseEntity<?> handleInfrastructureFailureException(InfrastructureFailureException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse.buildFromInternalException(e));
    }
}
