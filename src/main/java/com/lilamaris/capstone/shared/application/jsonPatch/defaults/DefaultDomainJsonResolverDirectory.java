package com.lilamaris.capstone.shared.application.jsonPatch.defaults;

import com.fasterxml.jackson.databind.JsonNode;
import com.lilamaris.capstone.shared.application.jsonPatch.DomainJsonResolver;
import com.lilamaris.capstone.shared.application.jsonPatch.DomainJsonResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultDomainJsonResolverDirectory implements DomainJsonResolverDirectory {
    private final Map<DomainType, DomainJsonResolver> resolvers;

    public DefaultDomainJsonResolverDirectory(
            List<DomainJsonResolver> resolvers
    ) {
        this.resolvers = resolvers.stream().collect(
                Collectors.toUnmodifiableMap(
                        DomainJsonResolver::support,
                        Function.identity()
                ));
    }

    @Override
    public JsonNode resolve(DomainRef ref) {
        DomainType type = ref.type();
        return Optional.ofNullable(resolvers.get(type))
                .map(resolver -> resolver.resolve(ref))
                .orElseThrow(() -> new UnsupportedOperationException(String.format(
                        "Unknown domain type '%s' and id '%s'",
                        ref.type().name(),
                        ref.id().asString()
                )));
    }
}
