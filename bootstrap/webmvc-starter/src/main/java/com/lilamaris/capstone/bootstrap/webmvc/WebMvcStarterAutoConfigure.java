package com.lilamaris.capstone.bootstrap.webmvc;

import com.lilamaris.capstone.bootstrap.webmvc.advice.GlobalRestControllerExceptionAdvice;
import com.lilamaris.capstone.bootstrap.webmvc.advice.handler.*;
import com.lilamaris.capstone.bootstrap.webmvc.advice.resolver.DefaultHttpStatusResolver;
import com.lilamaris.capstone.bootstrap.webmvc.advice.resolver.HttpStatusResolver;
import com.lilamaris.capstone.bootstrap.webmvc.advice.resolver.TypeUriResolver;
import com.lilamaris.capstone.bootstrap.webmvc.advice.response.ProblemDetailFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(RestControllerAdvice.class)
@ConditionalOnProperty(
        prefix = "capstone.bootstrap.webmvc",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(WebMvcStarterProperties.class)
public class WebMvcStarterAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    TypeUriResolver typeUriResolver(WebMvcStarterProperties properties) {
        return new TypeUriResolver(properties.error().typeBaseUri().toString());
    }

    @Bean
    @ConditionalOnMissingBean
    ProblemDetailFactory problemDetailFactory(TypeUriResolver typeUriResolver) {
        return new ProblemDetailFactory(typeUriResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    HttpStatusResolver httpStatusResolver() {
        return new DefaultHttpStatusResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    ApplicationExceptionHandler applicationExceptionHandler(
            ProblemDetailFactory problemDetailFactory,
            HttpStatusResolver httpStatusResolver
    ) {
        return new ApplicationExceptionHandler(problemDetailFactory, httpStatusResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    BadRequestExceptionHandler badRequestExceptionHandler(ProblemDetailFactory problemDetailFactory) {
        return new BadRequestExceptionHandler(problemDetailFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    AccessDeniedExceptionHandler accessDeniedExceptionHandler(ProblemDetailFactory problemDetailFactory) {
        return new AccessDeniedExceptionHandler(problemDetailFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    IllegalStateExceptionHandler illegalStateExceptionHandler(ProblemDetailFactory problemDetailFactory) {
        return new IllegalStateExceptionHandler(problemDetailFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    FallbackExceptionHandler fallbackExceptionHandler(ProblemDetailFactory problemDetailFactory) {
        return new FallbackExceptionHandler(problemDetailFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    GlobalRestControllerExceptionAdvice globalRestControllerExceptionAdvice(
            ApplicationExceptionHandler applicationExceptionHandler,
            BadRequestExceptionHandler badRequestExceptionHandler,
            AccessDeniedExceptionHandler accessDeniedExceptionHandler,
            IllegalStateExceptionHandler illegalStateExceptionHandler,
            FallbackExceptionHandler fallbackExceptionHandler
    ) {
        return new GlobalRestControllerExceptionAdvice(
                applicationExceptionHandler,
                badRequestExceptionHandler,
                accessDeniedExceptionHandler,
                illegalStateExceptionHandler,
                fallbackExceptionHandler
        );
    }
}
