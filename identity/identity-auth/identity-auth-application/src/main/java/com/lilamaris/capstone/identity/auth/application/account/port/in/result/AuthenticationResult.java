package com.lilamaris.capstone.identity.auth.application.account.port.in.result;

import com.lilamaris.capstone.identity.auth.domain.account.User;
import com.lilamaris.capstone.identity.auth.domain.role.UserGrantedRole;
import com.lilamaris.capstone.identity.core.role.NamespaceRole;
import com.lilamaris.capstone.identity.core.role.SimpleNamespaceRole;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record AuthenticationResult(
        UUID userId,
        String nickname,
        Set<NamespaceRole> grantedRoles
) {
    public static AuthenticationResult from(User user, List<UserGrantedRole> userGrantedRoles) {
        Set<NamespaceRole> grantedRoles = userGrantedRoles.stream()
                .map(userGrantedRole ->
                        SimpleNamespaceRole.of(userGrantedRole.getNamespace(), userGrantedRole.getRole())
                )
                .collect(Collectors.toUnmodifiableSet());
        return new AuthenticationResult(user.getId(), user.getNickname(), grantedRoles);
    }
}
