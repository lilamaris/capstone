package com.lilamaris.capstone.scenario.auth.application.port.in;

import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface SessionIssuer {
    AuthResult.Token issue(ExternalizableId principalId, String displayName);
}
