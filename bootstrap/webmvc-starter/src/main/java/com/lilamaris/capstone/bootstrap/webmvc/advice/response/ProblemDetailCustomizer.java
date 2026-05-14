package com.lilamaris.capstone.bootstrap.webmvc.advice.response;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ProblemDetail;

@FunctionalInterface
public interface ProblemDetailCustomizer<E extends Throwable> {
    void customize(ProblemDetail problemDetail, E exception, HttpServletRequest request);
}