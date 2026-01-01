package com.lilamaris.capstone.shared.infrastructure.web.config;

import com.lilamaris.capstone.shared.application.exception.*;
import com.lilamaris.capstone.shared.infrastructure.web.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    @ExceptionHandler(ApplicationInvariantException.class)
    public ResponseEntity<?> handleInvariantException(ApplicationInvariantException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse.buildFromInternalException(e));
    }

    @ExceptionHandler(DomainViolationException.class)
    public ResponseEntity<?> handleDomainViolationException(DomainViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.buildFromInternalException(e));
    }

    @ExceptionHandler(InfrastructureFailureException.class)
    public ResponseEntity<?> handleInfrastructureFailureException(InfrastructureFailureException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse.buildFromInternalException(e));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> handleResourceAlreadyExistException(ResourceAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.buildFromInternalException(e));
    }

    @ExceptionHandler(ResourceForbiddenException.class)
    public ResponseEntity<?> handleResourceForbiddenException(ResourceForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionResponse.buildFromInternalException(e));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.buildFromInternalException(e));
    }
}
