package com.lilamaris.capstone.delta.application.port.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import org.springframework.lang.Nullable;

public interface DeltaIssuer {
    DeltaEntry issue(
            DomainRef resource,
            ExternalizableId slotId,
            @Nullable JsonNode state,
            @Nullable JsonPatch patch
    );
}
