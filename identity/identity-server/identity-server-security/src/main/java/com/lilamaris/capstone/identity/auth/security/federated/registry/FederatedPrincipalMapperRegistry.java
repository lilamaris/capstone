package com.lilamaris.capstone.identity.auth.security.federated.registry;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FederatedPrincipalMapperRegistry {
    private final Map<String, FederatedPrincipalMapper> registry;

    public FederatedPrincipalMapperRegistry(List<FederatedPrincipalMapper> mappers) {
        Preconditions.requireNonNull(mappers, "mappers");

        this.registry = mappers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        FederatedPrincipalMapper::key,
                        Function.identity()
                ));
    }

    public FederatedPrincipalMapper getByKey(String key) {
        return registry.get(key);
    }
}
