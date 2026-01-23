package com.lilamaris.capstone.snapshot.application.service;

import com.lilamaris.capstone.shared.application.context.ActorContext;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAuthorizer;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDescriptionMetadata;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import com.lilamaris.capstone.snapshot.application.policy.previlege.SnapshotAction;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotCreator;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotEntry;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotReader;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotUpdater;
import com.lilamaris.capstone.snapshot.application.port.out.SnapshotStore;
import com.lilamaris.capstone.snapshot.domain.Snapshot;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class SnapshotService implements
        SnapshotReader,
        SnapshotCreator,
        SnapshotUpdater {
    private final SnapshotStore snapshotStore;
    private final DomainRefResolverDirectory refDir;

    private final IdGenerationDirectory ids;
    private final ResourceAuthorizer authorizer;

    private Supplier<ResourceNotFoundException> resourceNotFound(SnapshotId id) {
        return () -> new ResourceNotFoundException(String.format(
                "Snapshot with id '%s' not found", id
        ));
    }

    @Override
    public List<SnapshotEntry> resolveRefs(List<DomainRef> refs) {
        var ids = refDir.resolve(refs, SnapshotId.class);
        return snapshotStore.getByIds(ids).stream().map(SnapshotEntry::from).toList();
    }

    @Override
    public SnapshotEntry resolveRef(DomainRef ref) {
        var id = refDir.resolve(ref, SnapshotId.class);
        return snapshotStore.getById(id).map(SnapshotEntry::from)
                .orElseThrow(resourceNotFound(id));
    }

    @Override
    public SnapshotEntry getById(SnapshotId id) {
        return snapshotStore.getById(id)
                .map(SnapshotEntry::from)
                .orElseThrow(resourceNotFound(id));
    }

    @Override
    public List<SnapshotEntry> getByIds(List<SnapshotId> ids) {
        return snapshotStore.getByIds(ids).stream().map(SnapshotEntry::from).toList();
    }

    @Override
    public SnapshotEntry create(String title, String details) {
        var snapshot = Snapshot.create(
                ids.next(SnapshotId.class),
                title,
                details
        );
        var created = snapshotStore.save(snapshot);
        return SnapshotEntry.from(created);
    }

    @Override
    public SnapshotEntry update(SnapshotId id, String title, String details) {
        var actor = ActorContext.get();
        authorizer.authorize(actor, id.ref(), SnapshotAction.UPDATE_METADATA);

        var snapshot = snapshotStore.getById(id).orElseThrow(resourceNotFound(id));
        snapshot.updateDescription(new DefaultDescriptionMetadata(title, details));

        return SnapshotEntry.from(snapshot);
    }
}
