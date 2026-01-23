package com.lilamaris.capstone.delta.application.service;

import com.lilamaris.capstone.delta.application.port.in.DeltaEntry;
import com.lilamaris.capstone.delta.application.port.in.DeltaExistenceChecker;
import com.lilamaris.capstone.delta.application.port.in.DeltaIssuer;
import com.lilamaris.capstone.delta.application.port.in.DeltaRevoker;
import com.lilamaris.capstone.delta.application.port.out.DeltaStore;
import com.lilamaris.capstone.delta.domain.Delta;
import com.lilamaris.capstone.delta.domain.id.DeltaId;
import com.lilamaris.capstone.shared.application.exception.ResourceAlreadyExistsException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeltaService implements
        DeltaExistenceChecker,
        DeltaIssuer,
        DeltaRevoker {
    private final DeltaStore deltaStore;

    private final DomainRefResolverDirectory refDir;
    private final IdGenerationDirectory ids;

    @Override
    public boolean isExist(DomainRef resourceRef, ExternalizableId slotRef) {
        return deltaStore.isExists(resourceRef, slotRef);
    }

    @Override
    public DeltaEntry issue(DomainRef resource, ExternalizableId slotId, String jsonPatch) {
        var exists = deltaStore.isExists(resource, slotId);
        if (exists) throw new ResourceAlreadyExistsException(String.format(
                "Delta already exists with resource type '%s' and id '%s' in slot id '%s'.",
                resource.type().name(),
                resource.id().asString(),
                slotId.asString()
        ));

        var delta = Delta.create(
                ids.next(DeltaId.class),
                slotId,
                resource,
                jsonPatch
        );

        var saved = deltaStore.save(delta);

        return DeltaEntry.from(saved);
    }

    @Override
    public void revoke(DomainRef resource, ExternalizableId slotId) {
        deltaStore.delete(resource, slotId);
    }
}
