package com.lilamaris.capstone.access_control.application.result;

import com.lilamaris.capstone.access_control.domain.AccessControl;
import com.lilamaris.capstone.access_control.domain.id.AccessControlId;
import com.lilamaris.capstone.shared.application.result.ActorResult;
import com.lilamaris.capstone.shared.application.result.AuditResult;
import com.lilamaris.capstone.shared.application.result.DomainRefResult;

public class AccessControlResult {
    public record Command(
            AccessControlId id,
            ActorResult actor,
            DomainRefResult resource,
            String scopedRole,
            AuditResult audit
    ) {
        public static Command from(AccessControl domain) {
            return new Command(
                    domain.id(),
                    ActorResult.from(domain.getActor()),
                    DomainRefResult.from(domain.getResource()),
                    domain.getScopedRole(),
                    AuditResult.from(domain)
            );
        }
    }

    public record Query(
            AccessControlId id,
            ActorResult actor,
            DomainRefResult resource,
            String scopedRole,
            AuditResult audit
    ) {
        public static Query from(AccessControl domain) {
            return new Query(
                    domain.id(),
                    ActorResult.from(domain.getActor()),
                    DomainRefResult.from(domain.getResource()),
                    domain.getScopedRole(),
                    AuditResult.from(domain)
            );
        }
    }
}
