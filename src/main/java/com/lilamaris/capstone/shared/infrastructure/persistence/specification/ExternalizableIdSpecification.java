package com.lilamaris.capstone.shared.infrastructure.persistence.specification;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaExternalizableId;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.function.BiFunction;

public class ExternalizableIdSpecification {
    public static <T> Specification<T> eqExternalizableId(
            ExternalizableId id,
            BiFunction<Root<T>, CriteriaBuilder, Path<JpaExternalizableId>> pathBiFunction
    ) {
        return (root, query, cb) -> {
            var path = pathBiFunction.apply(root, cb);

            return cb.equal(
                    path.get("id"), id.asString()
            );
        };
    }

    public static <T> Specification<T> inExternalizableIds(
            Collection<? extends ExternalizableId> ids,
            BiFunction<Root<T>, CriteriaBuilder, Path<JpaExternalizableId>> pathBiFunction
    ) {
        return (root, query, cb) -> {
            if (ids == null || ids.isEmpty()) {
                return cb.disjunction();
            }

            var path = pathBiFunction.apply(root, cb).get("id");
            var inClause = cb.in(path);

            for (var id : ids) {
                inClause.value(id.asString());
            }

            return inClause;
        };
    }
}
