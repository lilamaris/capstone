package com.lilamaris.capstone.bootstrap.webmvc.advice.response;

import com.lilamaris.capstone.bootstrap.webmvc.advice.resolver.TypeUriResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;


@RequiredArgsConstructor
public class ProblemDetailFactory {
    private final TypeUriResolver uriResolver;

    public ProblemDetail build(
            HttpStatusCode status,
            String errorName,
            String detail
    ) {
        return build(status, errorName, errorName, detail);
    }

    public ProblemDetail build(
            HttpStatusCode status,
            String errorName,
            String code,
            String detail
    ) {
        var problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(errorName);
        problem.setType(uriResolver.resolve(errorName));
        problem.setProperty("code", code);

        return problem;
    }
}
