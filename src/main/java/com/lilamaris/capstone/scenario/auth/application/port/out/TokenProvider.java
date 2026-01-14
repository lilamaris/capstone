package com.lilamaris.capstone.scenario.auth.application.port.out;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface TokenProvider {
    TokenEntry getRefreshToken(ExternalizableId userId);
}
