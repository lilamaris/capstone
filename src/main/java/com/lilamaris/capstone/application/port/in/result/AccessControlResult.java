package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.access_control.AccessControl;
import com.lilamaris.capstone.domain.embed.DomainRef;
import com.lilamaris.capstone.domain.user.User;
import lombok.Builder;

public class AccessControlResult {
    @Builder
    public record Command(
            AccessControl.Id id,
            User.Id userId,
            DomainRef resource,
            String scopedRole,
            AuditResult audit
    ) {
        public static Command from(AccessControl domain) {
            return Command.builder()
                    .id(domain.id())
                    .userId(domain.userId())
                    .resource(domain.resource())
                    .scopedRole(domain.scopedRole())
//                    .audit(AuditResult.from(domain.audit()))
                    .build();
        }
    }

    @Builder
    public record Query(
            AccessControl.Id id,
            User.Id userId,
            DomainRef resource,
            String scopedRole,
            AuditResult audit
    ) {
        public static Query from(AccessControl domain) {
            return Query.builder()
                    .id(domain.id())
                    .userId(domain.userId())
                    .resource(domain.resource())
                    .scopedRole(domain.scopedRole())
//                    .audit(AuditResult.from(domain.audit()))
                    .build();
        }
    }
}
