package com.lilamaris.capstone.snapshot.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.metadata.DescriptionMetadata;
import com.lilamaris.capstone.snapshot.domain.Snapshot;

public record SnapshotEntry(
        DomainRef ref,
        DescriptionMetadata descriptionMetadata
) {
    public static SnapshotEntry from(Snapshot snapshot) {
        return new SnapshotEntry(
                snapshot.id().ref(),
                snapshot.descriptionMetadata()
        );
    }
}
