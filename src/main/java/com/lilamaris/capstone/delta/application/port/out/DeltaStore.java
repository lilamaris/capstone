package com.lilamaris.capstone.delta.application.port.out;

import com.lilamaris.capstone.delta.domain.Delta;
import com.lilamaris.capstone.delta.domain.id.DeltaId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

import java.util.List;
import java.util.Optional;

public interface DeltaStore {
    boolean isExists(
            DomainRef resource,
            ExternalizableId slotId
    );

    Optional<Delta> getById(DeltaId id);

    List<Delta> getByIds(List<DeltaId> ids);

    List<Delta> getBySlotId(ExternalizableId slotId);

    Optional<Delta> getBySlotIdAndResource(ExternalizableId slotId, DomainRef resource);

    List<Delta> getBySlotIds(List<ExternalizableId> slotIds);

    List<Delta> getBySlotIdsAndResource(List<ExternalizableId> slotIds, DomainRef resource);

    Delta save(Delta delta);

    void delete(
            DomainRef resource,
            ExternalizableId slotId
    );

    void deleteById(DeltaId id);
}
