package com.lilamaris.capstone.academiccatalog.persistence.shared.time.predicate;

import com.lilamaris.capstone.academiccatalog.domain.shared.time.EmbeddableInstantRange;
import com.lilamaris.capstone.academiccatalog.domain.shared.time.TemporalRange;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.PredicateSpecification;

import java.time.Instant;
import java.util.function.Function;

public class InstantRangePathPredicates {
    public static <T> PredicateSpecification<T> sameRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.and(
                    cb.equal(path.get("startAt"), range.start()),
                    cb.equal(path.get("endAt"), range.end())
            );
        };
    }

    public static <T> PredicateSpecification<T> beforeRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.lessThan(path.get("endAt"), range.start());
        };
    }

    public static <T> PredicateSpecification<T> beforeAtPoint(
            Instant point,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.lessThan(path.get("endAt"), point);
        };
    }

    public static <T> PredicateSpecification<T> immediatelyBeforeRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.equal(path.get("endAt"), range.start());
        };
    }

    public static <T> PredicateSpecification<T> endsAtPoint(
            Instant point,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.equal(path.get("endAt"), point);
        };
    }

    public static <T> PredicateSpecification<T> startBeforeRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.lessThan(path.get("startAt"), range.start());
        };
    }

    public static <T> PredicateSpecification<T> endAfterRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.greaterThan(path.get("endAt"), range.end());
        };
    }

    public static <T> PredicateSpecification<T> immediatelyAfterRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.equal(path.get("startAt"), range.end());
        };
    }

    public static <T> PredicateSpecification<T> startAtPoint(
            Instant point,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.equal(path.get("startAt"), point);
        };
    }

    public static <T> PredicateSpecification<T> afterRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.greaterThan(path.get("startAt"), range.end());
        };
    }

    public static <T> PredicateSpecification<T> afterAtPoint(
            Instant point,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.greaterThan(path.get("startAt"), point);
        };
    }

    public static <T> PredicateSpecification<T> containsRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.and(
                    cb.lessThanOrEqualTo(path.get("startAt"), range.start()),
                    cb.greaterThanOrEqualTo(path.get("endAt"), range.end())
            );
        };
    }

    public static <T> PredicateSpecification<T> containsPoint(
            Instant point,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.and(
                    cb.lessThanOrEqualTo(path.get("startAt"), point),
                    cb.greaterThan(path.get("endAt"), point)
            );
        };
    }

    public static <T> PredicateSpecification<T> containedByRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.and(
                    cb.greaterThanOrEqualTo(path.get("startAt"), range.start()),
                    cb.lessThanOrEqualTo(path.get("endAt"), range.end())
            );
        };
    }

    public static <T> PredicateSpecification<T> overlapsRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<EmbeddableInstantRange>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.and(
                    cb.lessThan(path.get("startAt"), range.end()),
                    cb.greaterThan(path.get("endAt"), range.start())
            );
        };
    }
}
