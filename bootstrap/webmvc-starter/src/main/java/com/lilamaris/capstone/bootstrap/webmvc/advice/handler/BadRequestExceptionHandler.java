package com.lilamaris.capstone.bootstrap.webmvc.advice.handler;

import com.lilamaris.capstone.bootstrap.webmvc.advice.response.ProblemDetailFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RequiredArgsConstructor
public class BadRequestExceptionHandler implements WebMvcExceptionHandler<Exception> {
    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class
    })
    @Override
    public ProblemDetail handle(Exception exception, HttpServletRequest request) {
        log.warn("Bad request. type={}, path={}, message={}",
                exception.getClass().getSimpleName(),
                request.getRequestURI(),
                exception.getMessage());

        return problemDetailFactory.build(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다.");
    }
}
