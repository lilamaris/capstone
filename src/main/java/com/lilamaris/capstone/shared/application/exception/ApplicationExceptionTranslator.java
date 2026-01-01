package com.lilamaris.capstone.shared.application.exception;

import com.lilamaris.capstone.shared.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.shared.domain.exception.DomainIllegalStateException;
import com.lilamaris.capstone.shared.domain.exception.DomainInvariantException;
import com.lilamaris.capstone.timeline.domain.exception.TimelineDomainException;
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
    @Around("@within(org.springframework.stereotype.Service)")
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

        } catch (InfrastructureFailureException e) {
            log.warn("Infrastructure failure: {}: {}", e.getCode(), e.getMessage(), e);
            throw e;

        } catch (DomainIllegalArgumentException e) {
            log.warn("Domain violation - {}: {}", e.getCode(), e.getMessage(), e);
            throw new DomainViolationException(e.getCode(), "Invalid request");

        } catch (DomainIllegalStateException e) {
            log.warn("Domain violation - {}: {}", e.getCode(), e.getMessage(), e);
            throw new DomainViolationException(e.getCode(), "Invalid state for this operation");

        } catch (TimelineDomainException e) {
            log.warn("Domain violation - {}: {}", e.getCode(), e.getMessage());
            throw new DomainViolationException(e.getCode(), "Invalid operation");

        } catch (DomainInvariantException e) {
            log.error("Domain violation - {}: {}", e.getCode(), e.getMessage());
            throw new DomainViolationException(e.getCode(), "Internal server error");

        } catch (DataIntegrityViolationException e) {
            log.error("Database constraint violation: {}", e.getMessage());
            throw new InfrastructureFailureException("Internal server error");

        } catch (Exception e) {
            log.error("Unexpected application error: {}", e.getMessage());
            throw new InfrastructureFailureException("Internal server error");

        }
    }
}