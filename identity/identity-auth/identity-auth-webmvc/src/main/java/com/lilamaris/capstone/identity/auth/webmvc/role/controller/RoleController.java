package com.lilamaris.capstone.identity.auth.webmvc.role.controller;

import com.lilamaris.capstone.identity.auth.application.role.port.in.GrantRoleUseCase;
import com.lilamaris.capstone.identity.auth.application.role.port.in.ListUserGrantedRoleUseCase;
import com.lilamaris.capstone.identity.auth.application.role.port.in.RevokeRoleUseCase;
import com.lilamaris.capstone.identity.auth.application.role.port.in.query.ListUserGrantedRoleQuery;
import com.lilamaris.capstone.identity.auth.application.role.port.in.result.UserGrantedRoleResult;
import com.lilamaris.capstone.identity.auth.webmvc.role.request.GrantRoleRequest;
import com.lilamaris.capstone.identity.auth.webmvc.role.request.RevokeRoleRequest;
import com.lilamaris.capstone.identity.core.actor.context.ActorContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Role", description = "권한 관리 API")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final GrantRoleUseCase grantRoleUseCase;
    private final RevokeRoleUseCase revokeRoleUseCase;
    private final ListUserGrantedRoleUseCase listUserGrantedRoleUseCase;

    private final ActorContextHolder actorContextHolder;

    @Operation(summary = "권한 부여", description = "특정 사용자에게 특정 서비스에 특정 역할을 부여합니다.")
    @ApiResponse(responseCode = "200", description = "부여 성공")
    @PreAuthorize("hasAuthority('role.grant')")
    @PostMapping
    public ResponseEntity<UserGrantedRoleResult> grantRole(
            @RequestBody GrantRoleRequest body
    ) {
        var actor = actorContextHolder.getActor();
        var command = body.toCommand(actor);
        var result = grantRoleUseCase.grant(command);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "권한 회수", description = "특정 사용자가 갖고 있는 특성 서비스의 특정 역할을 회수합니다.")
    @ApiResponse(responseCode = "200", description = "회수 성공")
    @PreAuthorize("hasAuthority('role.revoke')")
    @DeleteMapping
    public ResponseEntity<Void> revokeRole(
            @RequestBody RevokeRoleRequest body
    ) {
        var actor = actorContextHolder.getActor();
        var command = body.toCommand(actor);
        revokeRoleUseCase.revoke(command);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "사용자 권한 조회", description = "특정 사용자가 보유 중인 모든 권한을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserGrantedRoleResult>> listUserRole(
            @Parameter(description = "조회할 사용자 ID", example = "00000000-0000-0000-0000-000000000001")
            @PathVariable("userId") UUID userId
    ) {
        var query = new ListUserGrantedRoleQuery(userId);
        var result = listUserGrantedRoleUseCase.list(query);
        return ResponseEntity.ok(result);
    }
}