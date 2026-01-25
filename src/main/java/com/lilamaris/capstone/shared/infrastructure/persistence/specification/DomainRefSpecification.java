package com.lilamaris.capstone.shared.infrastructure.persistence.specification;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaDomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.function.BiFunction;

public class DomainRefSpecification {
    public static <T> Specification<T> eqDomainRef(
            DomainRef ref,
            BiFunction<Root<T>, CriteriaBuilder, Path<JpaDomainRef>> pathBiFunction
    ) {
        return (root, query, cb) -> {
            var path = pathBiFunction.apply(root, cb);

            return cb.and(
                    cb.equal(path.get("type").get("name"), ref.type().name()),
                    cb.equal(path.get("id"), ref.id().asString())
            );
        };
    }

    public static <T> Specification<T> eqDomainType(
            DomainType type,
            BiFunction<Root<T>, CriteriaBuilder, Path<JpaDomainRef>> pathBiFunction
    ) {
        return (root, query, cb) -> {
            var path = pathBiFunction.apply(root, cb);

            return cb.equal(path.get("type").get("name"), type.name());
        };
    }
}
