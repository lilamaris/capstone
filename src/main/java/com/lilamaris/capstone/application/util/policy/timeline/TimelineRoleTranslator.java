package com.lilamaris.capstone.application.util.policy.timeline;

import com.lilamaris.capstone.application.util.policy.RoleTranslator;
import org.springframework.stereotype.Component;

@Component
public class TimelineRoleTranslator implements RoleTranslator {
    @Override
    public TimelineRole toRoleName(String scopedRole) {
        return TimelineRole.valueOf(scopedRole);
    }
}
