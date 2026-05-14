package com.lilamaris.capstone.bootstrap.webmvc.advice.resolver;

import com.lilamaris.capstone.kernel.core.exception.ApplicationErrorCode;
import org.springframework.http.HttpStatus;

public class DefaultHttpStatusResolver implements HttpStatusResolver {

    @Override
    public HttpStatus resolve(ApplicationErrorCode errorCode) {
        var name = errorCode.name();

        if (name.contains("AUTHENTICATION_FAILED")) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (name.contains("FORBIDDEN") || name.contains("UNAUTHORIZED")) {
            return HttpStatus.FORBIDDEN;
        }
        if (name.contains("NOT_FOUND")) {
            return HttpStatus.NOT_FOUND;
        }
        if (name.contains("ALREADY_EXISTS") || name.contains("DUPLICATED") || name.contains("INVALID")) {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
