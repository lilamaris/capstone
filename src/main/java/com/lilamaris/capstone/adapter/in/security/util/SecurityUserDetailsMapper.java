package com.lilamaris.capstone.adapter.in.security.util;

import com.lilamaris.capstone.adapter.in.security.SecurityUserDetails;
import com.lilamaris.capstone.application.util.auth.JwtUtil;
import com.lilamaris.capstone.domain.model.capstone.user.Role;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
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
