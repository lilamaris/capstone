package com.lilamaris.capstone.bootstrap.webmvc.advice.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ProblemDetail;

public interface WebMvcExceptionHandler<T extends Throwable> {
    ProblemDetail handle(T exception, HttpServletRequest request);
}
