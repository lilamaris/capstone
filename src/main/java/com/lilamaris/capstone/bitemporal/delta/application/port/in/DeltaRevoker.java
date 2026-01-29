package com.lilamaris.capstone.bitemporal.delta.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface DeltaRevoker {
    void revoke(
            DomainRef resource,
            ExternalizableId slotId
    );
}
