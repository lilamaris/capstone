package com.lilamaris.capstone.identity.client.jwt;

import com.lilamaris.capstone.identity.core.actor.Actor;
import com.lilamaris.capstone.identity.core.actor.Capability;
import com.lilamaris.capstone.identity.core.actor.SimpleActor;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import com.lilamaris.capstone.identity.core.role.NamespaceRole;
import com.lilamaris.capstone.identity.core.role.NamespaceRoleDeserializer;
import com.lilamaris.capstone.identity.core.role.RoleCapabilityResolver;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import com.lilamaris.capstone.kernel.core.namespace.RunningNamespaceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ActorAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final String SCOPES_CLAIM = "scopes";

    private final NamespaceRoleDeserializer deserializer;
    private final RunningNamespaceContext namespaceContext;
    private final RoleCapabilityResolver capabilityResolver;

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        var subject = tryAccessClaim(source, JwtClaimAccessor::getSubject);
        var scopes = tryAccessClaim(source, this::getScopes);

        Set<CanonicalRole> currentRoles = scopes.stream()
                .map(deserializer::deserialize)
                .filter(nr -> namespaceContext.isSame(nr.namespace()))
                .map(NamespaceRole::role)
                .collect(Collectors.toUnmodifiableSet());

        Set<Capability> currentCapabilities = capabilityResolver.resolve(currentRoles);

        Actor actor = SimpleActor.of(subject, currentCapabilities);

        var authorities = buildAuthorities(currentRoles, currentCapabilities);

        return new ActorAuthenticationToken(actor, source, authorities);
    }

    private Set<GrantedAuthority> buildAuthorities(Set<CanonicalRole> roles, Set<Capability> capabilities) {
        var roleAuthorities = roles.stream()
                .map(role -> "ROLE_" + role.name());
        var capabilityAuthorities = capabilities.stream()
                .map(Capability::scope);

        return Stream.concat(roleAuthorities, capabilityAuthorities)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    private Set<String> getScopes(JwtClaimAccessor accessor) {
        var raw = accessor.getClaim(SCOPES_CLAIM);
        if (!(raw instanceof Collection<?> claims)) return Set.of();
        return claims.stream()
                .map(String.class::cast)
                .map(String::trim)
                .filter(claim -> !claim.isBlank())
                .collect(Collectors.toUnmodifiableSet());
    }

    private <T> T tryAccessClaim(Jwt source, Function<JwtClaimAccessor, T> accessor) {
        Preconditions.requireNonNull(source, "source");
        Preconditions.requireNonNull(accessor, "accessor");

        return accessor.apply(source);
    }
}