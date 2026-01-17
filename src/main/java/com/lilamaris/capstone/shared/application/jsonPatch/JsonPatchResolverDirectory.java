package com.lilamaris.capstone.shared.application.jsonPatch;

import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface JsonPatchResolverDirectory {
    JsonPatchResolver<?> resolverOf(DomainType type);
}
