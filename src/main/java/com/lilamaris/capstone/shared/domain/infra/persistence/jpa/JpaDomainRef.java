package com.lilamaris.capstone.shared.domain.infra.persistence.jpa;

import com.lilamaris.capstone.shared.domain.defaults.DefaultDomainRef;
import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.infra.ToPojo;
import com.lilamaris.capstone.shared.domain.type.CoreDomainType;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;


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
        if (externalizableId == null) {
            externalizableId = new DefaultExternalizableId(id);
        }
        return externalizableId;
    }


    @Override
    public DefaultDomainRef toPOJO() {
        return new DefaultDomainRef(type, id());
    }
}
