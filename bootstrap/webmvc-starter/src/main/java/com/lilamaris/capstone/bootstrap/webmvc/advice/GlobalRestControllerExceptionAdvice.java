package com.lilamaris.capstone.bootstrap.webmvc.advice;

import com.lilamaris.capstone.bootstrap.webmvc.advice.handler.AccessDeniedExceptionHandler;
import com.lilamaris.capstone.bootstrap.webmvc.advice.handler.ApplicationExceptionHandler;
import com.lilamaris.capstone.bootstrap.webmvc.advice.handler.BadRequestExceptionHandler;
import com.lilamaris.capstone.bootstrap.webmvc.advice.handler.FallbackExceptionHandler;
import com.lilamaris.capstone.bootstrap.webmvc.advice.handler.IllegalStateExceptionHandler;
import com.lilamaris.capstone.kernel.core.exception.ApplicationBaseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalRestControllerExceptionAdvice {
    private final ApplicationExceptionHandler applicationExceptionHandler;
    private final BadRequestExceptionHandler badRequestExceptionHandler;
    private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;
    private final IllegalStateExceptionHandler illegalStateExceptionHandler;
    private final FallbackExceptionHandler fallbackExceptionHandler;

    @ExceptionHandler(ApplicationBaseException.class)
    public ProblemDetail handleApplicationException(
            ApplicationBaseException exception,
            HttpServletRequest request
    ) {
        return applicationExceptionHandler.handle(exception, request);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class
    })
    public ProblemDetail handleBadRequest(Exception exception, HttpServletRequest request) {
        return badRequestExceptionHandler.handle(exception, request);
    }

    @ExceptionHandler({
            AccessDeniedException.class,
            AuthorizationDeniedException.class
    })
    public ProblemDetail handleAccessDenied(AccessDeniedException exception, HttpServletRequest request) {
        return accessDeniedExceptionHandler.handle(exception, request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException exception, HttpServletRequest request) {
        return illegalStateExceptionHandler.handle(exception, request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleFallback(Exception exception, HttpServletRequest request) {
        return fallbackExceptionHandler.handle(exception, request);
    }
}
