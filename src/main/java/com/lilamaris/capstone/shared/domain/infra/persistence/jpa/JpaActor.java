package com.lilamaris.capstone.shared.domain.infra.persistence.jpa;

import com.lilamaris.capstone.shared.domain.defaults.DefaultActor;
import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.shared.domain.event.actor.ActorType;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.infra.ToPojo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

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
        if (externalizableId == null) {
            externalizableId = new DefaultExternalizableId(id);
        }
        return externalizableId;
    }

    @Override
    public CanonicalActor toPOJO() {
        return new DefaultActor(type, id());
    }
}
