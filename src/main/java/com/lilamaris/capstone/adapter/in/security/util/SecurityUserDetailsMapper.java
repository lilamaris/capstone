package com.lilamaris.capstone.adapter.in.security.util;

import com.lilamaris.capstone.adapter.in.security.SecurityUserDetails;
import com.lilamaris.capstone.application.util.JwtUtil;
import com.lilamaris.capstone.domain.user.Role;
import com.lilamaris.capstone.domain.user.User;
import io.jsonwebtoken.Claims;

public class SecurityUserDetailsMapper {
    public static SecurityUserDetails fromClaims(Claims claims) {
        User.Id userId = User.Id.from(claims.getSubject());
        String displayName = claims.get(JwtUtil.DISPLAY_NAME_KEY, String.class);
        String roleValue = claims.get(JwtUtil.AUTHORITIES_KEY, String.class);
        Role role = Role.valueOf(roleValue);

        return SecurityUserDetails.builder().userId(userId).displayName(displayName).role(role).build();
    }
}
