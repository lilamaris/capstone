package com.lilamaris.capstone.domain.model.common.infra.persistence.jpa;

import com.lilamaris.capstone.domain.model.common.defaults.DefaultDomainRef;
import com.lilamaris.capstone.domain.model.common.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;
import com.lilamaris.capstone.domain.model.common.domain.type.CoreDomainType;
import com.lilamaris.capstone.domain.model.common.domain.type.DomainType;
import com.lilamaris.capstone.domain.model.common.infra.ToPojo;
import jakarta.persistence.*;
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

    @Transient
    private ExternalizableId externalizableId;

    protected JpaDomainRef(CoreDomainType type, String id) {
        this.type = requireField(type, "ref_type");
        this.id = requireField(id, "ref_id");
        externalizableId = new DefaultExternalizableId(id);
    }

    public static JpaDomainRef from(DomainRef ref) {
        return new JpaDomainRef(
                (CoreDomainType) ref.type(),
                ref.id().asString()
        );
    }

    public static JpaDomainRef from(DomainType type, ExternalizableId id) {
        return new JpaDomainRef(
                (CoreDomainType) type,
                id.asString()
        );
    }

    @Override
    public DomainType type() {
        return type;
    }

    @Override
    public ExternalizableId id() {
        return externalizableId;
    }


    @Override
    public DefaultDomainRef toPOJO() {
        return new DefaultDomainRef(type, externalizableId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainRef other)) return false;
        return type.equals(other.type()) && id.equals(other.id().asString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }
}
