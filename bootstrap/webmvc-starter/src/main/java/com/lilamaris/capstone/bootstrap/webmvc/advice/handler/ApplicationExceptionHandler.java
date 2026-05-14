package com.lilamaris.capstone.bootstrap.webmvc.advice.handler;

import com.lilamaris.capstone.bootstrap.webmvc.advice.resolver.HttpStatusResolver;
import com.lilamaris.capstone.bootstrap.webmvc.advice.response.ProblemDetailFactory;
import com.lilamaris.capstone.kernel.core.exception.ApplicationBaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@RequiredArgsConstructor
public class ApplicationExceptionHandler implements WebMvcExceptionHandler<ApplicationBaseException> {
    private final ProblemDetailFactory problemDetailFactory;
    private final HttpStatusResolver statusResolver;

    @ExceptionHandler(ApplicationBaseException.class)
    @Override
    public ProblemDetail handle(ApplicationBaseException exception, HttpServletRequest request) {
        var errorCode = exception.getErrorCode();
        var status = statusResolver.resolve(errorCode);

        log.warn("handle application exception. errorCode={}, path={}", errorCode, request.getRequestURI());

        return problemDetailFactory.build(status, errorCode.name(), errorCode.getCode(), errorCode.getMessage());
    }
}
