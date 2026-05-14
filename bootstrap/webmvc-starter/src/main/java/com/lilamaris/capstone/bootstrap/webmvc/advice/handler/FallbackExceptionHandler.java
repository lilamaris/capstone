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
public class FallbackExceptionHandler implements WebMvcExceptionHandler<Exception> {
    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler(Exception.class)
    @Override
    public ProblemDetail handle(Exception exception, HttpServletRequest request) {
        log.error("Unhandled exception. type={}, path={}, message={}",
                exception.getClass().getSimpleName(),
                request.getRequestURI(),
                exception.getMessage(),
                exception);

        return problemDetailFactory.build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "알 수 없는 오류가 발생했습니다.");
    }
}
