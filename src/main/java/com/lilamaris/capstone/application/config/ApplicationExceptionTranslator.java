package com.lilamaris.capstone.application.config;

import com.lilamaris.capstone.application.exception.*;
import com.lilamaris.capstone.domain.embed.exception.EffectiveDomainException;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.domain.exception.DomainIllegalStateException;
import com.lilamaris.capstone.domain.exception.DomainInvariantException;
import com.lilamaris.capstone.domain.timeline.exception.TimelineDomainException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ApplicationExceptionTranslator {
    @Around("execution(* com.lilamaris.capstone.application..*(..))")
    public Object translateException(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();

        } catch (ResourceAlreadyExistsException e) {
            log.warn("Resource already exists: {}: {}", e.getCode(), e.getMessage(), e);
            throw e;

        } catch (ResourceNotFoundException e) {
            log.warn("Resource not found: {}: {}", e.getCode(), e.getMessage(), e);
            throw e;

        } catch (ResourceForbiddenException e) {
            log.warn("Resource forbidden: {}: {}", e.getCode(), e.getMessage(), e);
            throw e;

        } catch (ApplicationInvariantException e) {
            log.warn("Application invariant: {}: {}", e.getCode(), e.getMessage(), e);
            throw e;

        } catch (DomainIllegalArgumentException e) {
            log.warn("Domain violation - {}: {}", e.getCode(), e.getMessage(), e);
            throw new DomainViolationException(e.getCode(), "Invalid request");

        } catch (DomainIllegalStateException e) {
            log.warn("Domain violation - {}: {}", e.getCode(), e.getMessage(), e);
            throw new DomainViolationException(e.getCode(), "Invalid state for this operation");

        } catch (TimelineDomainException | EffectiveDomainException e) {
            log.warn("Domain violation - {}: {}", e.getCode(), e.getMessage(), e);
            throw new DomainViolationException(e.getCode(), "Invalid operation");

        } catch (DomainInvariantException e) {
            log.error("Domain violation - {}: {}", e.getCode(), e.getMessage(), e);
            throw new DomainViolationException(e.getCode(), "Internal server error");

        } catch (DataIntegrityViolationException e) {
            log.error("Database constraint violation: {}", e.getMessage(), e);
            throw new InfrastructureFailureException("Internal server error");

        } catch (Exception e) {
            log.error("Unexpected application error: {}", e.getMessage(), e);
            throw new InfrastructureFailureException("Internal server error");

        }
    }
}