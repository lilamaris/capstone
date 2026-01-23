package com.lilamaris.capstone.delta.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface DeltaIssuer {
    DeltaEntry issue(
            DomainRef resource,
            ExternalizableId slotId,
            String jsonPatch
    );
}
