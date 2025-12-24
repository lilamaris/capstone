package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.model.capstone.user.Role;
import com.lilamaris.capstone.domain.model.capstone.user.User;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
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
