package com.lilamaris.capstone.snapshot.application.service;

import com.lilamaris.capstone.shared.application.context.ActorContext;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAuthorizer;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDescriptionMetadata;
import com.lilamaris.capstone.snapshot.application.policy.previlege.SnapshotAction;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotCommandUseCase;
import com.lilamaris.capstone.snapshot.application.port.out.SnapshotStore;
import com.lilamaris.capstone.snapshot.application.result.SnapshotResult;
import com.lilamaris.capstone.snapshot.domain.Snapshot;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SnapshotCommandService implements SnapshotCommandUseCase {
    private final SnapshotStore snapshotStore;

    private final IdGenerationDirectory ids;
    private final ResourceAuthorizer authorizer;

    @Override
    public SnapshotResult.Command create(String title, String details) {
        var snapshot = Snapshot.create(
                ids.next(SnapshotId.class),
                title,
                details
        );
        var created = snapshotStore.save(snapshot);
        return SnapshotResult.Command.from(created);
    }

    @Override
    public SnapshotResult.Command update(SnapshotId id, String title, String details) {
        var actor = ActorContext.get();
        authorizer.authorize(actor, id.ref(), SnapshotAction.UPDATE_METADATA);

        var snapshot = snapshotStore.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Snapshot with ref '%s' not found.", id))
        );
        snapshot.updateDescription(new DefaultDescriptionMetadata(title, details));

        return SnapshotResult.Command.from(snapshot);
    }
}
