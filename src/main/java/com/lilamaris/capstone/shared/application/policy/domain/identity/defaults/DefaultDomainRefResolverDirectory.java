package com.lilamaris.capstone.shared.application.policy.domain.identity.defaults;

import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDomainRef;
import com.lilamaris.capstone.shared.domain.id.DomainId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
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
    public <T extends DomainId<?>> T resolve(DomainRef ref, Class<T> expect) {
        var resolver = resolverOf(ref.type());
        return resolveId(resolver, ref, expect);
    }

    @Override
    public <T extends DomainId<?>> T resolve(ExternalizableId externalId, DomainType type, Class<T> expect) {
        var resolver = resolverOf(type);
        var ref = new DefaultDomainRef(type, externalId);
        return resolveId(resolver, ref, expect);
    }

    private <T extends DomainId<?>> DomainRefResolver<T> resolverOf(DomainType type) {
        @SuppressWarnings("unchecked")
        var resolver = (DomainRefResolver<T>) resolvers.get(type);
        if (resolver == null) {
            throw new UnsupportedOperationException(String.format(
                    "Unsupported type for '%s'", type
            ));
        }
        return resolver;
    }

    private <T extends DomainId<?>> T resolveId(DomainRefResolver<?> resolver, DomainRef ref, Class<T> expect) {
        var id = resolver.resolve(ref);
        if (!id.getClass().equals(expect)) {
            throw new IllegalArgumentException(String.format(
                    "Resolved id not matched with expect. resolved: '%s', expect: '%s'",
                    id.getClass(), expect
            ));
        }
        return expect.cast(id);
    }
}
