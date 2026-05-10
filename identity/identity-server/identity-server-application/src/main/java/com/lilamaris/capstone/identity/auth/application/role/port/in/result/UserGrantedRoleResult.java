package com.lilamaris.capstone.identity.auth.application.role.port.in.result;

import com.lilamaris.capstone.identity.auth.domain.role.UserGrantedRole;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;

import java.time.Instant;
import java.util.UUID;

public record UserGrantedRoleResult(
        UUID userId,
        String namespace,
        CanonicalRole role,
        Instant createdAt
) {
    public static UserGrantedRoleResult from(UserGrantedRole userGrantedRole) {
        return new UserGrantedRoleResult(
                userGrantedRole.getUserId(),
                userGrantedRole.getNamespace().getName(),
                userGrantedRole.getRole(),
                userGrantedRole.getCreatedAt()
        );
    }
}
