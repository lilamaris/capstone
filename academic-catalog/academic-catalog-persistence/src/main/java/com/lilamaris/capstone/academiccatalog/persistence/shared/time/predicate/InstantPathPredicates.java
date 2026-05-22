package com.lilamaris.capstone.academiccatalog.persistence.shared.time.predicate;

import com.lilamaris.capstone.academiccatalog.domain.shared.time.TemporalRange;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.PredicateSpecification;

import java.time.Instant;
import java.util.function.Function;

public class InstantPathPredicates {
    public static <T> PredicateSpecification<T> beforeRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<Instant>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.lessThan(path, range.start());
        };
    }

    public static <T> PredicateSpecification<T> equalsRangeStart(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<Instant>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.equal(path, range.start());
        };
    }

    public static <T> PredicateSpecification<T> equalsRangeEnd(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<Instant>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.equal(path, range.end());
        };
    }

    public static <T> PredicateSpecification<T> afterRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<Instant>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.greaterThan(path, range.end());
        };
    }

    public static <T> PredicateSpecification<T> containedInRange(
            TemporalRange<Instant> range,
            Function<From<?, T>, Path<Instant>> pathFunction
    ) {
        return (from, cb) -> {
            var path = pathFunction.apply(from);

            return cb.and(
                    cb.greaterThanOrEqualTo(path, range.start()),
                    cb.lessThan(path, range.end())
            );
        };
    }
}
