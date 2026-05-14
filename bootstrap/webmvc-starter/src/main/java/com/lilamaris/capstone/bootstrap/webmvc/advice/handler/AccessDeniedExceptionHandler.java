package com.lilamaris.capstone.bootstrap.webmvc.advice.handler;

import com.lilamaris.capstone.bootstrap.webmvc.advice.response.ProblemDetailFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler implements WebMvcExceptionHandler<AccessDeniedException> {
    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler(AccessDeniedException.class)
    @Override
    public ProblemDetail handle(AccessDeniedException exception, HttpServletRequest request) {
        log.warn("Access denied. path={}, message={}", request.getRequestURI(), exception.getMessage());

        return problemDetailFactory.build(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "접근 권한이 없습니다.");
    }
}
