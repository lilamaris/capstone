package com.lilamaris.capstone.auth.scenario.auth.application.port.out;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface AuthTokenRegistrar {
    AuthRefreshTokenRegisterEntry register(ExternalizableId subject);
}
