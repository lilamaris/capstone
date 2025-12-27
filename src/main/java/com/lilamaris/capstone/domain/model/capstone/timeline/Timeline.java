package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.domain.exception.DomainIllegalStateException;
import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import com.lilamaris.capstone.domain.model.capstone.timeline.event.TimelineCreated;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.common.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.domain.model.common.persistence.jpa.JpaDescriptionMetadata;
import com.lilamaris.capstone.domain.model.common.event.aggregate.CollectedDomainEvent;
import com.lilamaris.capstone.domain.model.common.event.DomainEvent;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import com.lilamaris.capstone.domain.timeline.exception.TimelineDomainException;
import com.lilamaris.capstone.domain.timeline.exception.TimelineErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "timeline_root")
@EntityListeners(AuditingEntityListener.class)
public class Timeline implements Identifiable<TimelineId> {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();
    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private TimelineId id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "timeline_id", nullable = false)
    private List<Snapshot> snapshotList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "timeline_id", nullable = false)
    private List<SnapshotLink> snapshotLinkList;
    @Embedded
    private JpaDescriptionMetadata descriptionMetadata;
    @Transient
    private Map<SnapshotId, Snapshot> snapshotMap;
    @Transient
    private Map<SnapshotLinkId, SnapshotLink> snapshotLinkMap;
    @Transient
    private Map<SnapshotId, SnapshotLink> snapshotLinkByDescendant;
    @Transient
    private Map<SnapshotId, SnapshotLink> snapshotLinkByAncestor;

    protected Timeline(
            TimelineId id,
            List<Snapshot> snapshotList,
            List<SnapshotLink> snapshotLinkList,
            JpaDescriptionMetadata descriptionMetadata
    ) {
        this.id = requireField(id, "id");
        this.snapshotList = requireField(snapshotList, "snapshotList");
        this.snapshotLinkList = requireField(snapshotLinkList, "snapshotLinkList");
        this.descriptionMetadata = requireField(descriptionMetadata, "descriptionMetadata");
        checkFieldInvariants();
    }

    public static Timeline create(
            String title,
            String details,
            Supplier<TimelineId> idSupplier
    ) {
        var id = idSupplier.get();
        var descriptionMetadata = JpaDescriptionMetadata.create(title, details);
        var timeline = new Timeline(
                id,
                new ArrayList<>(),
                new ArrayList<>(),
                descriptionMetadata
        );
        timeline.onCreated();
        return timeline;
    }

    @DomainEvents
    CollectedDomainEvent domainEvents() {
        return new CollectedDomainEvent(List.copyOf(eventList));
    }

    @AfterDomainEventPublication
    void clearDomainEvents() {
        eventList.clear();
    }

    @PostLoad
    private void onLoad() {
        propagateToTransient();
    }

    private void onCreated() {
        var event = new TimelineCreated(id, Instant.now());
        eventList.add(event);
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

    public void migrate(
            Instant txAt,
            Instant validAt,
            String details,
            Supplier<SnapshotId> snapshotIdSupplier,
            Supplier<SnapshotLinkId> snapshotLinkIdSupplier
    ) {
        if (snapshotList.isEmpty()) {
            createInitialSnapshot(txAt, validAt, details, snapshotIdSupplier, snapshotLinkIdSupplier);
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

        sourceSnapshot.close(EffectiveSelector.TX, txAt);
        var newTx = Effective.create(txAt, Effective.MAX);
        var newValidSplit = sourceSnapshot.getValid().splitAt(validAt);

        var newSnapshotLeft = Snapshot.create(snapshotIdSupplier.get(), newTx, newValidSplit.left(), id, 0, details);
        var newSnapshotRight = Snapshot.create(snapshotIdSupplier.get(), newTx, newValidSplit.right(), id, 0, details);

        var newSnapshotLeftLink = SnapshotLink.create(snapshotLinkIdSupplier.get(), id, sourceSnapshot.id(), newSnapshotLeft.id());
        var newSnapshotRightLink = SnapshotLink.create(snapshotLinkIdSupplier.get(), id, newSnapshotLeft.id(), newSnapshotRight.id());

        snapshotList.addAll(List.of(newSnapshotLeft, newSnapshotRight));
        snapshotLinkList.addAll(List.of(newSnapshotLeftLink, newSnapshotRightLink));

        eventList.addAll(newSnapshotLeft.pullEvent());
        eventList.addAll(newSnapshotRight.pullEvent());
        eventList.addAll(newSnapshotLeftLink.pullEvent());
        eventList.addAll(newSnapshotRightLink.pullEvent());

        propagateToTransient();
    }

    public void mergeValidRange(
            Instant txAt,
            Effective validRange,
            String description,
            Supplier<SnapshotId> snapshotIdSupplier,
            Supplier<SnapshotLinkId> snapshotLinkIdSupplier
    ) {
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

        var mergedSnapshot = Snapshot.create(snapshotIdSupplier.get(), mergedSnapshotTx, mergedSnapshotValid, id, 0, description);
        var mergedSnapshotLink = SnapshotLink.create(snapshotLinkIdSupplier.get(), id, earliestSnapshot.id(), mergedSnapshot.id());

        snapshotList.add(mergedSnapshot);
        snapshotLinkList.add(mergedSnapshotLink);

        sourceSnapshots.forEach(s -> eventList.addAll(s.pullEvent()));
        eventList.addAll(mergedSnapshot.pullEvent());
        eventList.addAll(mergedSnapshotLink.pullEvent());
        propagateToTransient();
    }

    private void createInitialSnapshot(
            Instant txAt,
            Instant validAt,
            String description,
            Supplier<SnapshotId> snapshotIdSupplier,
            Supplier<SnapshotLinkId> snapshotLinkIdSupplier
    ) {
        var tx = Effective.create(txAt, Effective.MAX);
        var valid = Effective.create(validAt, Effective.MAX);

        var snapshot = Snapshot.create(snapshotIdSupplier.get(), tx, valid, id, 0, description);
        var link = SnapshotLink.create(snapshotLinkIdSupplier.get(), id, null, snapshot.id());

        snapshotList.add(snapshot);
        snapshotLinkList.add(link);
        eventList.addAll(snapshot.pullEvent());
        eventList.addAll(link.pullEvent());
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

    private record LookupBuildResult(
            Map<SnapshotId, Snapshot> snapshotById,
            Map<SnapshotLinkId, SnapshotLink> linkById,
            Map<SnapshotId, SnapshotLink> linkByDescendant,
            Map<SnapshotId, SnapshotLink> linkByAncestor
    ) {
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

        LinkLookup build() {
            return new LinkLookup(
                    Map.copyOf(byId),
                    Map.copyOf(byDescendant),
                    Map.copyOf(byAncestor)
            );
        }

        record LinkLookup(
                Map<SnapshotLinkId, SnapshotLink> byId,
                Map<SnapshotId, SnapshotLink> byDescendant,
                Map<SnapshotId, SnapshotLink> byAncestor
        ) {
        }
    }
}

