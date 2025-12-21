package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.domain.common.impl.DefaultAuditableDomain;
import com.lilamaris.capstone.domain.common.mixin.Identifiable;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.domain.exception.DomainIllegalStateException;
import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.timeline.exception.TimelineDomainException;
import com.lilamaris.capstone.domain.timeline.exception.TimelineErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Getter
@ToString
@Entity
@Table(name = "timeline_root")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timeline extends DefaultAuditableDomain implements Identifiable<TimelineId>
{
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    private TimelineId id;

    @OneToMany(mappedBy = "timelineId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Snapshot> snapshotList;

    @OneToMany(mappedBy = "timelineId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SnapshotLink> snapshotLinkList;

    @Transient
    private Map<SnapshotId, Snapshot> snapshotMap;

    @Transient
    private Map<SnapshotLinkId, SnapshotLink> snapshotLinkMap;

    @Transient
    private Map<SnapshotId, SnapshotLink> snapshotLinkByDescendant;

    @Transient
    private Map<SnapshotId, SnapshotLink> snapshotLinkByAncestor;

    public static Timeline create() {
        return new Timeline(TimelineId.newId(), new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public final TimelineId id() {
        return id;
    }

    public List<Snapshot> getSnapshotsWithOpenTx() {
        return snapshotList.stream().filter(s -> s.getTx().isOpen()).toList();
    }

    public List<Snapshot> getSnapshotsWithClosedTx() {
        return snapshotList.stream().filter(s -> !s.getTx().isOpen()).toList();
    }

    public void migrate(Instant txAt, Instant validAt, String description) {
        if (snapshotList.isEmpty()) {
            createInitialSnapshot(txAt, validAt, description);
            return;
        }

        var sourceSnapshot = filterSnapshotValid(validAt).orElseThrow(() -> new TimelineDomainException(
                TimelineErrorCode.NO_AVAILABLE_SNAPSHOT,
                String.format(
                        "Invalid filter argument: no snapshot match the given criteria valid '%s'", validAt
                )
        ));

        var newSnapshotLeft = sourceSnapshot.closeTxAndNext(txAt);
        var newSnapshotRight = newSnapshotLeft.closeValidAndNext(validAt);

        var newSnapshotLeftLink = SnapshotLink.create(id, sourceSnapshot.id(), newSnapshotLeft.id());
        var newSnapshotRightLink = SnapshotLink.create(id, newSnapshotLeft.id(), newSnapshotRight.id());

        newSnapshotLeft.setDescription(description);
        newSnapshotRight.setDescription(description);

        snapshotList.addAll(List.of(newSnapshotLeft, newSnapshotRight));
        snapshotLinkList.addAll(List.of(newSnapshotLeftLink, newSnapshotRightLink));
        propagateToTransient();
    }

    private void createInitialSnapshot(Instant txAt, Instant validAt, String description) {
        var tx = Effective.create(txAt, Effective.MAX);
        var valid = Effective.create(validAt, Effective.MAX);
        var snapshot = Snapshot.create(tx, valid, id, description);
        var link = SnapshotLink.create(id, null, snapshot.id());

        snapshotList.add(snapshot);
        snapshotLinkList.add(link);
        propagateToTransient();
    }

    private Optional<Snapshot> filterSnapshotValid(Instant at) {
        var result = snapshotList.stream()
                .filter(s -> s.getTx().isOpen())
                .filter(s -> s.getValid().contains(at))
                .toList();
        if (result.size() > 1) {
            throw new DomainIllegalStateException(
                    String.format("Invalid snapshot state: more then one snapshot exists for the same valid at: '%s'", at)
            );
        }

        return result.stream().findFirst();
    }

    private List<Snapshot> filterSnapshotValid(Effective range) {
        return snapshotList.stream()
                .filter(s -> s.getTx().isOpen())
                .filter(s -> s.getValid().isOverlap(range))
                .toList();
    }

    private void checkFieldInvariants() {
        if (snapshotList.size() != snapshotLinkList.size()) {
            throw new DomainIllegalStateException(
                    "Invalid snapshot state: snapshot count does not match snapshot link count."
            );
        }
    }

    private void propagateToTransient() {
        checkFieldInvariants();
        var lookup = buildSnapshotLookup();
        snapshotMap = lookup.snapshotById;
        snapshotLinkMap = lookup.linkById;
        snapshotLinkByDescendant = lookup.linkByDescendant;
        snapshotLinkByAncestor = lookup.linkByAncestor;
    }

    private record LookupBuildResult(
            Map<SnapshotId, Snapshot> snapshotById,
            Map<SnapshotLinkId, SnapshotLink> linkById,
            Map<SnapshotId, SnapshotLink> linkByDescendant,
            Map<SnapshotId, SnapshotLink> linkByAncestor
    ) {}

    private LookupBuildResult buildSnapshotLookup() {
        var linkLookup = snapshotLinkList.stream().collect(
                Collector.of(
                        LinkLookupBuilder::new,
                        LinkLookupBuilder::add,
                        LinkLookupBuilder::merge,
                        LinkLookupBuilder::build
                )
        );

        var snapshotLookup = snapshotList.stream().collect(Collectors.toMap(Snapshot::id, Function.identity()));

        return new LookupBuildResult(
                snapshotLookup,
                linkLookup.byId(),
                linkLookup.byDescendant(),
                linkLookup.byAncestor()
        );
    }

    private static final class LinkLookupBuilder {
        final Map<SnapshotLinkId, SnapshotLink> byId = new HashMap<>();
        final Map<SnapshotId, SnapshotLink> byDescendant = new HashMap<>();
        final Map<SnapshotId, SnapshotLink> byAncestor = new HashMap<>();

        private static <K, V> void putUnique(Map<K, V> map, K key, V value, String what) {
            var prev = map.putIfAbsent(key, value);
            if (prev != null) {
                throw new DomainIllegalStateException(String.format(
                        "SnapshotLink invariant violated: duplicate key for '%s': '%s'", what, key
                ));
            }
        }

        void add(SnapshotLink link) {
            putUnique(byId, link.id(), link, "link.id");
            putUnique(byDescendant, link.getDescendantSnapshotId(), link, "descendantSnapshotId");

            var ancestorId = link.getAncestorSnapshotId();
            if (ancestorId != null) {
                putUnique(byAncestor, ancestorId, link, "ancestorSnapshotId");
            }
        }

        // NOTE: must be sequential stream; merge() assumes no key conflicts
        void addAll(LinkLookupBuilder o) {
            byId.putAll(o.byId);
            byDescendant.putAll(o.byDescendant);
            byAncestor.putAll(o.byAncestor);
        }

        LinkLookupBuilder merge(LinkLookupBuilder o) {
            addAll(o);
            return this;
        }

        record LinkLookup(
                Map<SnapshotLinkId, SnapshotLink> byId,
                Map<SnapshotId, SnapshotLink> byDescendant,
                Map<SnapshotId, SnapshotLink> byAncestor
        ) {}

        LinkLookup build() {
            return new LinkLookup(
                    Map.copyOf(byId),
                    Map.copyOf(byDescendant),
                    Map.copyOf(byAncestor)
            );
        }
    }

    private Timeline(TimelineId id, List<Snapshot> snapshotList, List<SnapshotLink> snapshotLinkList) {
        if (id == null) throw new DomainIllegalArgumentException("Field 'id' must not be null.");
        if (snapshotList == null) throw new DomainIllegalArgumentException("Field 'snapshotList' must not be null.");
        if (snapshotLinkList == null) throw new DomainIllegalArgumentException("Field 'snapshotLinkList' must not be null.");

        this.id = id;
        this.snapshotList = snapshotList;
        this.snapshotLinkList = snapshotLinkList;

        checkFieldInvariants();
    }
}

