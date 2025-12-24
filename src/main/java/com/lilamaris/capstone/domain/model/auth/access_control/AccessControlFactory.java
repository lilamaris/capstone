package com.lilamaris.capstone.domain.model.auth.access_control;

import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.id.*;
import com.lilamaris.capstone.domain.model.common.id.impl.DefaultIdGenerateContext;
import com.lilamaris.capstone.domain.model.common.id.impl.JpaDomainRef;
import com.lilamaris.capstone.domain.model.common.type.CoreDomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccessControlFactory {
    private final IdGenerationContext idGenerationContext;

    public AccessControlFactory(
            IdGenerator idGenerator,
            RawGenerator<UUID> uuidRawGenerator
    ) {
        Map<IdSpec<?, ?>, RawGenerator<?>> map = Map.of(
                AccessControlId.SPEC, uuidRawGenerator
        );

        this.idGenerationContext = new DefaultIdGenerateContext(idGenerator, map);
    }

    public AccessControl create(UserId userId, CoreDomainType resourceType, DomainId<?> resourceId, String scopedRole) {
        var id = idGenerationContext.next(AccessControlId.SPEC);
        var ref = JpaDomainRef.from(resourceType, resourceId);
        return new AccessControl(id, userId, ref, scopedRole);
    }

    public AccessControl create(UserId userId, DomainRef resourceRef, String scopedRole) {
        var id = idGenerationContext.next(AccessControlId.SPEC);
        var ref = JpaDomainRef.from(resourceRef);
        return new AccessControl(id, userId, ref, scopedRole);
    }
}
