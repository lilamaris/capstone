package com.lilamaris.capstone.identity.auth.webmvc.role.request;

import com.lilamaris.capstone.identity.auth.application.role.port.in.command.GrantRoleCommand;
import com.lilamaris.capstone.identity.core.actor.Actor;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "권한 부여 요청")
public record GrantRoleRequest(
        @Schema(description = "사용자 Id", example = "00000000-0000-0000-0000-000000000001")
        UUID userId,
        @Schema(description = "서비스 명", example = "identity-auth")
        String namespaceName,
        @Schema(description = "역할", example = "USER")
        CanonicalRole role
) {
    public GrantRoleCommand toCommand(Actor actor) {
        return new GrantRoleCommand(userId, namespaceName, role, actor);
    }
}
