package com.lilamaris.capstone.bootstrap.webmvc.advice.resolver;

import com.lilamaris.capstone.kernel.core.exception.ApplicationErrorCode;
import org.springframework.http.HttpStatus;

public interface HttpStatusResolver {
    HttpStatus resolve(ApplicationErrorCode errorCode);
}
