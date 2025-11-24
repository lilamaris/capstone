package com.lilamaris.capstone.application.configuration;

import com.lilamaris.capstone.application.exception.ApplicationException;
import com.lilamaris.capstone.application.exception.ApplicationInvariantException;
import com.lilamaris.capstone.application.exception.DomainViolationException;
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
        } catch (ApplicationException e) {
            throw e;

        } catch (DomainIllegalArgumentException e) {
            log.warn("Domain violation - illegal argument: {}", e.getMessage(), e);
            throw new DomainViolationException("DOMAIN_ILLEGAL_ARGUMENT", "Invalid request");

        } catch (DomainIllegalStateException e) {
            log.warn("Domain violation - illegal state: {}", e.getMessage(), e);
            throw new DomainViolationException("DOMAIN_ILLEGAL_STATE", "Invalid state for this operation");

        } catch (TimelineDomainException | EffectiveDomainException e) {
            log.warn("Domain violation - {}: {}", e.getCode(), e.getMessage(), e);
            throw new DomainViolationException(e.getCode(), "Invalid operation");

        } catch (DomainInvariantException e) {
            log.error("Domain invariant violation: {}", e.getMessage(), e);
            throw new ApplicationInvariantException("Internal server error");

        } catch (DataIntegrityViolationException e) {
            log.error("Database constraint violation: {}", e.getMessage(), e);
            throw new ApplicationInvariantException("Internal server error");

        } catch (Exception e) {
            log.error("Unexpected application error: {}", e.getMessage(), e);
            throw new ApplicationInvariantException("Internal server error");

        }
    }
}