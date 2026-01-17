package com.lilamaris.capstone.shared.application.jsonPatch.defaults;

import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchResolver;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchResolverDirectory;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultJsonPatchResolverDirectory implements JsonPatchResolverDirectory {
    private final Map<DomainType, JsonPatchResolver<?>> resolvers;

    public DefaultJsonPatchResolverDirectory(
            List<JsonPatchResolver<?>> resolvers
    ) {
        this.resolvers = resolvers.stream().collect(
                Collectors.toUnmodifiableMap(
                        JsonPatchResolver::support,
                        Function.identity()
                ));
    }

    @Override
    public JsonPatchResolver<?> resolverOf(DomainType type) {
        return Optional.ofNullable(resolvers.get(type))
                .orElseThrow(() -> new UnsupportedOperationException("Unknown domain type: " + type));
    }
}
