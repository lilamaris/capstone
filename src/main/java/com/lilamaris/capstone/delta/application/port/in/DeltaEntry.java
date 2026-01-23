package com.lilamaris.capstone.delta.application.port.in;

import com.lilamaris.capstone.delta.domain.Delta;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;

public record DeltaEntry(
        DomainRef deltaRef,
        DomainRef resource,
        ExternalizableId slotId,
        String jsonPatch,
        AuditMetadata auditMetadata
) {
    public static DeltaEntry from(Delta delta) {
        return new DeltaEntry(
                delta.id().ref(),
                delta.getResource(),
                delta.getSlotId(),
                delta.getJsonPatch(),
                delta.auditMetadata()
        );
    }
}
