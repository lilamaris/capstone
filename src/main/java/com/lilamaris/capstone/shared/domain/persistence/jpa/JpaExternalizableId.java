package com.lilamaris.capstone.shared.domain.persistence.jpa;

import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.persistence.ToPojo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JpaExternalizableId implements ExternalizableId, ToPojo<ExternalizableId> {
    @Column(name = "external_id", nullable = false)
    private String id;

    protected JpaExternalizableId(String id) {
        this.id = requireField(id, "id");
    }

    public static JpaExternalizableId from(ExternalizableId id) {
        return new JpaExternalizableId(id.asString());
    }

    public static JpaExternalizableId from(String id) {
        return new JpaExternalizableId(id);
    }

    @Override
    public ExternalizableId toPOJO() {
        return DefaultExternalizableId.from(this);
    }

    @Override
    public String asString() {
        return id;
    }
}
