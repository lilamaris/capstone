package com.lilamaris.capstone.domain.model.common.impl.jpa;

import com.lilamaris.capstone.domain.model.common.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JpaDefaultDomainRef extends AbstractDomainRef {
    @Enumerated(EnumType.STRING)
    @Column(name = "ref_type", nullable = false)
    private CoreDomainType type;

    @Column(name = "ref_id", nullable = false)
    private String id;

    public static JpaDefaultDomainRef from(DomainRef ref) {
        if (ref instanceof JpaDefaultDomainRef jpaRef) {
            return jpaRef;
        }
        return new JpaDefaultDomainRef((CoreDomainType) ref.type(), ref.id());
    }

    public JpaDefaultDomainRef(CoreDomainType type, DomainId<?> id) {
        this.type = requireField(type, "type");
        this.id = requireField(id, "id").toString();
    }

    @Override
    public DomainType type() {
        return type;
    }

    @Override
    public DomainId<?> id() {
        throw new UnsupportedOperationException(
                "JpaDefaultDomainRef does not materialize DomainId"
        );
    }

    @Override
    protected Object identity() {
        return List.of(type, id);
    }

    public String refId() {
        return id;
    }
}
