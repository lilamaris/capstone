package com.lilamaris.capstone.delta.application.port.in;

import com.lilamaris.capstone.shared.domain.id.CanonicalExternalId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DeltaReader {
    List<DeltaEntry> getDeltaOfSlot(ExternalizableId slotId);

    Optional<DeltaEntry> getDeltaOfSlot(ExternalizableId slotId, DomainRef resource);

    Map<CanonicalExternalId, List<DeltaEntry>> getDeltaOfSlots(List<ExternalizableId> slotIds);

    Map<CanonicalExternalId, DeltaEntry> getDeltaOfSlots(List<ExternalizableId> slotIds, DomainRef resource);
}
