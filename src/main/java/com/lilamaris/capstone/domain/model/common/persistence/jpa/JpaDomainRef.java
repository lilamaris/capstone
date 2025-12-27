package com.lilamaris.capstone.domain.model.common.persistence.jpa;

import com.lilamaris.capstone.domain.model.common.defaults.DefaultDomainRef;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainId;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.mixin.ToPojo;
import com.lilamaris.capstone.domain.model.common.domain.type.CoreDomainType;
import com.lilamaris.capstone.domain.model.common.domain.type.DomainType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;


@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JpaDomainRef implements DomainRef, ToPojo<DomainRef> {
    @Enumerated(EnumType.STRING)
    @Column(name = "ref_type", nullable = false)
    private CoreDomainType type;

    @Column(name = "ref_id", nullable = false)
    private String id;

    protected JpaDomainRef(CoreDomainType type, String id) {
        this.type = requireField(type, "ref_type");
        this.id = requireField(id, "ref_id");
    }

    public static JpaDomainRef from(DomainRef ref) {
        return new JpaDomainRef(
                (CoreDomainType) ref.type(),
                ref.id()
        );
    }

    public static JpaDomainRef from(DomainType type, DomainId<?> id) {
        return new JpaDomainRef(
                (CoreDomainType) type,
                id.toString()
        );
    }

    @Override
    public DefaultDomainRef toPOJO() {
        return new DefaultDomainRef(type, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainRef other)) return false;
        return type.equals(other.type()) && id.equals(other.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }

    @Override
    public DomainType type() {
        throw new UnsupportedOperationException("JpaDomainRef is not materialized domain type");
    }

    @Override
    public String id() {
        throw new UnsupportedOperationException("JpaDomainRef is not materialized id");
    }
}
