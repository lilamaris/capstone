package com.lilamaris.capstone.bootstrap.webmvc.advice.handler;

import com.lilamaris.capstone.bootstrap.webmvc.advice.response.ProblemDetailFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@RequiredArgsConstructor
public class IllegalStateExceptionHandler implements WebMvcExceptionHandler<IllegalStateException> {
    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler(IllegalStateException.class)
    @Override
    public ProblemDetail handle(IllegalStateException exception, HttpServletRequest request) {
        log.error("Illegal state. path={}, message={}", request.getRequestURI(), exception.getMessage(), exception);

        return problemDetailFactory.build(HttpStatus.INTERNAL_SERVER_ERROR, "ILLEGAL_STATE", "요청 처리 중 오류가 발생했습니다.");
    }
}
