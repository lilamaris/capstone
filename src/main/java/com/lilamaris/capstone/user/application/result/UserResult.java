package com.lilamaris.capstone.user.application.result;

import com.lilamaris.capstone.user.domain.Role;
import com.lilamaris.capstone.user.domain.User;
import com.lilamaris.capstone.user.domain.id.UserId;
import lombok.Builder;

public class UserResult {
    @Builder
    public record Command(
            UserId id,
            String displayName,
            Role role
    ) {
        public static Command from(User domain) {
            return builder().id(domain.id()).displayName(domain.getDisplayName()).role(domain.getRole()).build();
        }
    }

    @Builder
    public record Query(
            UserId id,
            String displayName,
            Role role
    ) {
        public static Query from(User domain) {
            return builder().id(domain.id()).displayName(domain.getDisplayName()).role(domain.getRole()).build();
        }
    }
}
