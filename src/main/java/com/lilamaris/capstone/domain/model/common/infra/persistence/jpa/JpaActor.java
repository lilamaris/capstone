package com.lilamaris.capstone.domain.model.common.infra.persistence.jpa;

import com.lilamaris.capstone.domain.model.common.defaults.DefaultActor;
import com.lilamaris.capstone.domain.model.common.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.ActorType;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;
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
public class JpaActor implements CanonicalActor, ToPojo<CanonicalActor> {
    @Enumerated(EnumType.STRING)
    @Column(name = "actor_type", nullable = false)
    private ActorType type;

    @Column(name = "actor_id", nullable = false)
    private String id;

    @Transient
    private ExternalizableId externalizableId;

    protected JpaActor(ActorType type, String id) {
        this.type = requireField(type, "actorType");
        this.id = requireField(id, "actorId");
        externalizableId = new DefaultExternalizableId(id);
    }

    public static JpaActor from(CanonicalActor actor) {
        return new JpaActor(actor.type(), actor.id().asString());
    }

    @Override
    public ActorType type() {
        return type;
    }

    @Override
    public ExternalizableId id() {
        return externalizableId;
    }

    @Override
    public CanonicalActor toPOJO() {
        return new DefaultActor(type, externalizableId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CanonicalActor other)) return false;
        return type.equals(other.type()) && id.equals(other.id().asString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }
}
