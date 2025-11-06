package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.domain.TransitionLog;

import java.util.Optional;

public interface TransitionLogPort {
    Optional<TransitionLog> getById(TransitionLog.Id id);

    TransitionLog save(TransitionLog domain);
    void delete(TransitionLog.Id id);
}
