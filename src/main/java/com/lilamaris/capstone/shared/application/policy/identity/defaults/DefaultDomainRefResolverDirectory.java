package com.lilamaris.capstone.shared.application.policy.identity.defaults;

import com.lilamaris.capstone.shared.application.policy.identity.port.in.DomainRefResolver;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultDomainRefResolverDirectory implements DomainRefResolverDirectory {
    private final Map<DomainType, DomainRefResolver<?>> resolvers;

    public DefaultDomainRefResolverDirectory(List<DomainRefResolver<?>> resolvers) {
        this.resolvers = resolvers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        DomainRefResolver::supports,
                        Function.identity()
                ));
    }

    @Override
    public <T extends DomainId<?>> T resolve(DomainRef ref) {
        @SuppressWarnings("unchecked")
        var resolver = (DomainRefResolver<T>) resolvers.get(ref.type());
        if (resolver == null) {
            throw new IllegalArgumentException(
                    "Unsupported ref: " + ref
            );
        }

        return resolver.resolve(ref);
    }
}
