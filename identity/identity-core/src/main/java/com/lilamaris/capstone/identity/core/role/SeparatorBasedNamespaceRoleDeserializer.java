package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SeparatorBasedNamespaceRoleDeserializer implements NamespaceRoleDeserializer {
    public static final String SEPARATOR = ":";

    @Override
    public NamespaceRole deserialize(String source) {
        Preconditions.requireNonBlank(source, "source");

        var parts = source.split(Pattern.quote(SEPARATOR), -1);
        if (parts.length != 2 || Arrays.stream(parts).anyMatch(String::isBlank))
            throw new IllegalArgumentException("invalid namespace role format. expected '<namespace>:<role>', but was " + source);

        var namespace = SimpleApplicationNamespace.of(parts[0]);
        var role = tryParse(parts[1]);
        return SimpleNamespaceRole.of(namespace, role);
    }

    @Override
    public Set<NamespaceRole> deserialize(Collection<String> sources) {
        Preconditions.requireNonNull(sources, "sources");

        return sources.stream()
                .map(this::deserialize)
                .collect(Collectors.toUnmodifiableSet());
    }

    private CanonicalRole tryParse(String source) {
        try {
            return CanonicalRole.valueOf(source);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "invalid canonical role '%s'. expected one of: %s."
                            .formatted(source, Arrays.toString(CanonicalRole.values())),
                    e
            );
        }
    }
}
