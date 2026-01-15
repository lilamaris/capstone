package com.lilamaris.capstone.user.application.result;

import com.lilamaris.capstone.user.domain.User;
import com.lilamaris.capstone.user.domain.id.UserId;

public class UserResult {
    public record Command(
            UserId id,
            String displayName
    ) {
        public static Command from(User domain) {
            return new Command(
                    domain.id(),
                    domain.getDisplayName()
            );
        }
    }

    public record Query(
            UserId id,
            String displayName
    ) {
        public static Query from(User domain) {
            return new Query(
                    domain.id(),
                    domain.getDisplayName()
            );
        }
    }
}
