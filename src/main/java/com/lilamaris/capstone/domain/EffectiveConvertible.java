package com.lilamaris.capstone.domain;

import java.time.LocalDateTime;

public interface EffectiveConvertible {
    Effective convert();

    static EffectiveConvertible of(LocalDateTime at) {
        return () -> Effective.openAt(at);
    }

    static EffectiveConvertible of(Effective e) {
        return () -> e;
    }
}
