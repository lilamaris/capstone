package com.lilamaris.capstone.domain;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
public record TransitionLog (
    Id id,
    Context context,
    List<Target> targets
) implements BaseDomain<TransitionLog.Id, TransitionLog> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public record Context(
            BaseDomain.Id<?> rootId,
            String performedBy,
            LocalDateTime performedAt,
            String reason
    ) {}

    public record Target(
            BaseDomain.Id<?> domainId,
            String domainType,
            Transition.TransitionType commandType,
            List<Transition.Diff> diffList
    ) {}

    public static <T extends BaseDomain<?, ?>> TransitionLog fromTransition(Context ctx, List<Transition<T>> transitions) {
        List<Target> targets = transitions.stream()
                .flatMap(t -> toTarget(t).stream())
                .sorted(
                        Comparator.comparing(Target::domainType)
                                .thenComparing(t -> String.valueOf(t.domainId() != null ? t.domainId().asString() : ""))
                )
                .toList();
        return builder().context(ctx).targets(targets).build();
    }

    private static <T extends BaseDomain<?, ?>> List<Target> toTarget(Transition<T> transition) {
        var beforeMap = transition.before().stream()
                .collect(Collectors.toMap(d -> d.id(), Function.identity(),
                        (a, b) -> { throw new IllegalStateException("Duplicate before id: " + a.id()); }));
        var lifeCycle = transition.lifeCycleStrategy();
        var fieldExtractor = transition.fieldExtractor();

        return transition.after().stream()
                .map(d -> {
                    var id1 = d.id();
                    var reflectTarget = Optional.ofNullable(id1).map(beforeMap::get).orElse(null);

                    var diffs = Optional.ofNullable(reflectTarget).map(t -> getDiff(t, d, fieldExtractor)).orElse(asInitialState(d, fieldExtractor));
                    var transitionType = lifeCycle.apply(reflectTarget, d);
                    return new Target(id1, d.getDomainName(), transitionType, diffs);
                })
                .sorted(Comparator.comparing(Target::domainType))
                .toList();
    }

    private static <T extends BaseDomain<?, ?>> List<Transition.Diff> getDiff(
            T before, T after, Function<T, Map<String, Object>> fieldExtractor
    ) {
        Map<String, Object> b = new LinkedHashMap<>(fieldExtractor.apply(before));
        Map<String, Object> a = new LinkedHashMap<>(fieldExtractor.apply(after));

        Set<String> keys = new LinkedHashSet<>();
        keys.addAll(b.keySet());
        keys.addAll(a.keySet());

        List<Transition.Diff> list = new ArrayList<>();
        for (var key : keys) {
            Object bv = b.get(key);
            Object av = a.get(key);
            if (!Objects.equals(bv, av)) {
                list.add(new Transition.Diff(key, String.valueOf(bv), String.valueOf(av)));
            }
        }
        return list;
    }

    private static <T extends BaseDomain<?, ?>> List<Transition.Diff> asInitialState(
            T after, Function<T, Map<String, Object>> extractor
    ) {
        Map<String, Object> a = new LinkedHashMap<>(extractor.apply(after));
        List<Transition.Diff> list = new ArrayList<>(a.size());
        a.forEach((k, v) -> list.add(new Transition.Diff(k, null, v)));
        return list;
    }
}