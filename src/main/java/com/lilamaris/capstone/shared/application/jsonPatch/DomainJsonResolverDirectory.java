package com.lilamaris.capstone.shared.application.jsonPatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

public interface DomainJsonResolverDirectory {
    JsonNode resolve(DomainRef ref);
}
