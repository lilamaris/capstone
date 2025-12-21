package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.domain.common.impl.DefaultAuditableDomain;
import com.lilamaris.capstone.domain.common.mixin.Identifiable;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter(AccessLevel.PROTECTED)
@ToString
@Entity
@Table(name = "timeline_snapshot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Snapshot extends DefaultAuditableDomain implements Identifiable<SnapshotId> {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    private SnapshotId id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "from", column = @Column(name = "tx_from")),
            @AttributeOverride(name = "to", column = @Column(name = "tx_to")),
    })
    private Effective tx;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "from", column = @Column(name = "valid_from")),
            @AttributeOverride(name = "to", column = @Column(name = "valid_to")),
    })
    private Effective valid;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "timeline_id", insertable = false, updatable = false))
    private TimelineId timelineId;

    private String description;
    private Integer versionNo;

    @Override
    public final SnapshotId id() {
        return id;
    }

    protected static Snapshot create(Effective tx, Effective valid, TimelineId timelineId, String description) {
        return new Snapshot(SnapshotId.newId(), tx, valid, timelineId, 0, description);
    }

    protected Snapshot closeTxAndNext(Instant at) {
        var next = tx.closeAndNext(at);
        return Snapshot.create(next, valid, timelineId, description);
    }

    protected Snapshot closeValidAndNext(Instant at) {
        var next = valid.closeAndNext(at);
        return Snapshot.create(tx, next, timelineId, description);
    }

    protected void close(EffectiveSelector selector, Instant at) {
        if (selector == EffectiveSelector.TX) {
            tx.close(at);
        } else {
            valid.close(at);
        }
    }

    private void upgrade() {
        versionNo += 1;
    }

    private Snapshot(SnapshotId id, Effective tx, Effective valid, TimelineId timelineId, Integer versionNo, String description) {
        if (id == null) throw new DomainIllegalArgumentException("Field 'id' must not be null.");
        if (tx == null) throw new DomainIllegalArgumentException("Field 'tx' must not be null.");
        if (valid == null) throw new DomainIllegalArgumentException("Field 'valid' must not be null.");
        if (timelineId == null) throw new DomainIllegalArgumentException("Field 'timelineId' must not be null.");

        this.id = id;
        this.tx = tx;
        this.valid = valid;
        this.timelineId = timelineId;
        this.versionNo = versionNo;
        this.description = description;
    }
}
