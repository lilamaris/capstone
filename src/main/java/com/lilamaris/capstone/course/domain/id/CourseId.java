package com.lilamaris.capstone.course.domain.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.shared.domain.contract.Referenceable;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDomainRef;
import com.lilamaris.capstone.shared.domain.defaults.DefaultUuidDomainId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
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
        return DefaultDomainRef.from(AggregateDomainType.COURSE, this);
    }

    @Override
    public String asString() {
        return value.toString();
    }
}
