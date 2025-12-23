package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.domain.model.common.CoreDomainType;
import com.lilamaris.capstone.domain.model.common.DomainRef;
import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultAuditableDomain;
import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultDomainRef;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import com.lilamaris.capstone.domain.model.common.mixin.Referenceable;
import com.lilamaris.capstone.domain.exception.DomainIllegalStateException;
import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.timeline.exception.TimelineDomainException;
import com.lilamaris.capstone.domain.timeline.exception.TimelineErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@Entity
@Table(name = "timeline_root")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timeline extends JpaDefaultAuditableDomain implements Identifiable<TimelineId>, Referenceable {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    private TimelineId id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "timeline_id", nullable = false)
    private List<Snapshot> snapshotList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "timeline_id", nullable = false)
    private List<SnapshotLink> snapshotLinkList;

    private String description;

    @Transient
    private Map<SnapshotId, Snapshot> snapshotMap;

    @Transient
    private Map<SnapshotLinkId, SnapshotLink> snapshotLinkMap;

    @Transient
    private Map<SnapshotId, SnapshotLink> snapshotLinkByDescendant;

    @Transient
    private Map<SnapshotId, SnapshotLink> snapshotLinkByAncestor;

    @PostLoad
    private void onLoad() {
        propagateToTransient();
    }

    public static Timeline create(String description) {
        return new Timeline(TimelineId.newId(), new ArrayList<>(), new ArrayList<>(), description);
    }

    @Override
    public final TimelineId id() {
        return id;
    }

    @Override
    public DomainRef ref() {
        return new JpaDefaultDomainRef(CoreDomainType.TIMELINE, id);
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

        var maybeOneSnapshot = snapshotList.stream()
                .filter(ifEffectiveOpen(EffectiveSelector.TX, true))
                .filter(ifEffective(EffectiveSelector.VALID, validAt))
                .toList();

        if (maybeOneSnapshot.size() > 1) {
            throw new DomainIllegalStateException(
                    String.format("More then one snapshot exists for the same valid at: '%s'", validAt)
            );
        }

        var sourceSnapshot = maybeOneSnapshot.stream().findFirst().orElseThrow(() -> new TimelineDomainException(
                TimelineErrorCode.NO_AVAILABLE_SNAPSHOT,
                String.format(
                        "No snapshot match the given criteria valid '%s'", validAt
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

    public void mergeValidRange(Instant txAt, Effective validRange, String description) {
        var sourceSnapshots = snapshotList.stream()
                .filter(ifEffectiveOpen(EffectiveSelector.TX, true))
                .filter(ifEffective(EffectiveSelector.VALID, validRange))
                .sorted(sortAsc(EffectiveSelector.VALID))
                .toList();

        if (sourceSnapshots.isEmpty()) {
            throw new TimelineDomainException(TimelineErrorCode.NO_AVAILABLE_SNAPSHOT, String.format(
                    "No snapshot match the given criteria valid range '%s'", validRange
            ));
        }

        sourceSnapshots.forEach(s -> s.close(EffectiveSelector.TX, txAt));
        var earliestSnapshot = sourceSnapshots.getFirst();
        var latestSnapshot = sourceSnapshots.getLast();

        var startValidAt = earliestSnapshot.getValid().getFrom();
        var endValidAt = latestSnapshot.getValid().getTo();
        var mergedSnapshotValid = Effective.create(startValidAt, endValidAt);
        var mergedSnapshotTx = Effective.create(txAt, Effective.MAX);

        var mergedSnapshot = Snapshot.create(mergedSnapshotTx, mergedSnapshotValid, id, description);
        var mergedSnapshotLink = SnapshotLink.create(id, earliestSnapshot.id(), mergedSnapshot.id());

        snapshotList.add(mergedSnapshot);
        snapshotLinkList.add(mergedSnapshotLink);
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

    private Comparator<Snapshot> sortAsc(EffectiveSelector selector) {
        return selector == EffectiveSelector.TX
                ? Comparator.comparing(s -> s.getTx().getFrom())
                : Comparator.comparing(s -> s.getValid().getFrom());
    }

    private Predicate<Snapshot> ifEffectiveOpen(EffectiveSelector selector, boolean state) {
        return selector == EffectiveSelector.TX
                ? s -> s.getTx().isOpen() == state
                : s -> s.getValid().isOpen() == state;
    }

    private Predicate<Snapshot> ifEffective(EffectiveSelector selector, Instant at) {
        return selector == EffectiveSelector.TX
                ? s -> s.getTx().contains(at)
                : s -> s.getValid().contains(at);
    }

    private Predicate<Snapshot> ifEffective(EffectiveSelector selector, Effective range) {
        return selector == EffectiveSelector.TX
                ? s -> s.getTx().isOverlap(range)
                : s -> s.getValid().isOverlap(range);
    }

    private void checkFieldInvariants() {
        if (snapshotList.size() != snapshotLinkList.size()) {
            throw new DomainIllegalStateException(
                    "Snapshot count does not match snapshot link count."
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

    private Timeline(TimelineId id, List<Snapshot> snapshotList, List<SnapshotLink> snapshotLinkList, String description) {
        this.id                 = requireField(id, "id");
        this.snapshotList       = requireField(snapshotList, "snapshotList");
        this.snapshotLinkList   = requireField(snapshotLinkList, "snapshotLinkList");
        this.description        = requireField(description, "description");
        checkFieldInvariants();
    }
}

