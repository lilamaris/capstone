package com.lilamaris.capstone.domain.model.capstone.course.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.domain.model.common.defaults.DefaultDomainRef;
import com.lilamaris.capstone.domain.model.common.defaults.DefaultUuidDomainId;
import com.lilamaris.capstone.domain.model.common.domain.contract.Referenceable;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;
import com.lilamaris.capstone.domain.model.common.domain.type.ResourceDomainType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseId extends DefaultUuidDomainId implements Referenceable, ExternalizableId {
    @JsonValue
    protected UUID value;

    public CourseId(UUID value) {
        super(value);
    }

    public static CourseId newId() {
        return new CourseId(newUuid());
    }

    @Override
    public UUID value() {
        return value;
    }

    @Override
    protected void init(UUID value) {
        this.value = value;
    }

    @Override
    public DomainRef ref() {
        return new DefaultDomainRef(ResourceDomainType.COURSE, this);
    }

    @Override
    public String asString() {
        return value.toString();
    }
}
