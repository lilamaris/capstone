package com.lilamaris.capstone.snapshot.domain.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.shared.domain.defaults.DefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapshotId extends DefaultUuidDomainId {
    @JsonValue
    protected UUID value;

    public SnapshotId(UUID value) {
        super(value);
    }

    @Override
    public UUID value() {
        return value;
    }

    @Override
    protected void init(UUID value) {
        this.value = value;
    }
}
