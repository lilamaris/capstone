package com.lilamaris.capstone.domain.model.auth.access_control;

import com.lilamaris.capstone.application.util.generator.DefaultIdGenerationContext;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.id.DomainId;
import com.lilamaris.capstone.domain.model.common.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.id.IdGenerationContext;
import com.lilamaris.capstone.domain.model.common.id.RawGenerator;
import com.lilamaris.capstone.domain.model.common.id.impl.JpaDomainRef;
import com.lilamaris.capstone.domain.model.common.type.CoreDomainType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class AccessControlFactory {
    private final IdGenerationContext idGenerationContext;

    public AccessControlFactory(
            RawGenerator<UUID> uuidRawGenerator
    ) {
        Map<Class<?>, DefaultIdGenerationContext.Binding<?, ?>> map = Map.of(
                AccessControlId.class, DefaultIdGenerationContext.bind(SnapshotId::new, uuidRawGenerator)
        );

        this.idGenerationContext = new DefaultIdGenerationContext(map);
    }

    public AccessControl create(UserId userId, CoreDomainType resourceType, DomainId<?> resourceId, String scopedRole) {
        var id = idGenerationContext.next(AccessControlId.class);
        var ref = JpaDomainRef.from(resourceType, resourceId);
        return new AccessControl(id, userId, ref, scopedRole);
    }

    public AccessControl create(UserId userId, DomainRef resourceRef, String scopedRole) {
        var id = idGenerationContext.next(AccessControlId.class);
        var ref = JpaDomainRef.from(resourceRef);
        return new AccessControl(id, userId, ref, scopedRole);
    }
}
