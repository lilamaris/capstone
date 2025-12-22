package com.lilamaris.capstone.domain.model.util;

import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;

public class Validation {
    public static <T> T requireField(T value, String key) {
        if (value == null) throw new DomainIllegalArgumentException(
                String.format(
                        "Field '%s' is must not be null", key
                )
        );
        return value;
    }
}
