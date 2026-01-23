package com.lilamaris.capstone.user.application.port.in;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface UserExistenceChecker {
    boolean isExist(ExternalizableId id);
}
