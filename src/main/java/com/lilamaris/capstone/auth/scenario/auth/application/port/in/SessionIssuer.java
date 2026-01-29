package com.lilamaris.capstone.auth.scenario.auth.application.port.in;

import com.lilamaris.capstone.auth.scenario.auth.application.result.AuthResult;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface SessionIssuer {
    AuthResult.Token issue(ExternalizableId principalId, String displayName);
}
