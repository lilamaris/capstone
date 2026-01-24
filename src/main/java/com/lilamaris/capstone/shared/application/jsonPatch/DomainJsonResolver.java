package com.lilamaris.capstone.shared.application.jsonPatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface DomainJsonResolver {
    DomainType support();

    JsonNode resolve(DomainRef ref);
}
