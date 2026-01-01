package com.lilamaris.capstone.shared.infrastructure.security.util;

import com.lilamaris.capstone.orchestration.auth.util.JwtUtil;
import com.lilamaris.capstone.shared.infrastructure.security.SecurityUserDetails;
import com.lilamaris.capstone.user.domain.Role;
import com.lilamaris.capstone.user.domain.id.UserId;
import io.jsonwebtoken.Claims;

import java.util.UUID;

public class SecurityUserDetailsMapper {
    public static SecurityUserDetails fromClaims(Claims claims) {
        UserId userId = new UserId(UUID.fromString(claims.getSubject()));
        String displayName = claims.get(JwtUtil.DISPLAY_NAME_KEY, String.class);
        String roleValue = claims.get(JwtUtil.AUTHORITIES_KEY, String.class);
        Role role = Role.valueOf(roleValue);

        return SecurityUserDetails.builder().userId(userId).displayName(displayName).role(role).build();
    }
}
