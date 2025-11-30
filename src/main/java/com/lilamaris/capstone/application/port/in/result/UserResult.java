package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.user.Role;
import com.lilamaris.capstone.domain.user.User;
import lombok.Builder;

public class UserResult {
    @Builder
    public record Command(
            User.Id id,
            String displayName,
            Role role
    ) {
        public static Command from(User domain) {
            return builder().id(domain.id()).displayName(domain.displayName()).role(domain.role()).build();
        }
    }

    @Builder
    public record Query(
            User.Id id,
            String displayName,
            Role role
    ) {
        public static Query from(User domain) {
            return builder().id(domain.id()).displayName(domain.displayName()).role(domain.role()).build();
        }
    }
}
