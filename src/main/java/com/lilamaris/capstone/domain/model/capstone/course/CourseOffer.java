package com.lilamaris.capstone.domain.model.capstone.course;

import com.lilamaris.capstone.domain.model.capstone.course.id.CourseId;
import com.lilamaris.capstone.domain.model.capstone.course.id.CourseOfferId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.common.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.defaults.DefaultDomainRef;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import com.lilamaris.capstone.domain.model.common.mixin.Referenceable;
import com.lilamaris.capstone.domain.model.common.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.domain.model.common.type.ResourceDomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@Entity
@Table(name = "_course_offer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseOffer implements Identifiable<CourseOfferId>, Referenceable {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private CourseOfferId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "course_id", insertable = false, nullable = false, updatable = false))
    private CourseId courseId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "snapshot_id", insertable = false, nullable = false, updatable = false))
    private SnapshotId snapshotId;

    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    private Integer credit;

    private CourseOffer(CourseOfferId id, CourseId courseId, SnapshotId snapshotId, Integer credit) {
        this.id = requireField(id, "id");
        this.courseId = requireField(courseId, "courseId");
        this.snapshotId = requireField(snapshotId, "snapshotId");
        this.credit = requireField(credit, "credit");
    }

    public static CourseOffer create(CourseId courseId, SnapshotId snapshotId, Integer credit) {
        return new CourseOffer(CourseOfferId.newId(), courseId, snapshotId, credit);
    }

    @Override
    public CourseOfferId id() {
        return id;
    }

    @Override
    public DomainRef ref() {
        return DefaultDomainRef.from(ResourceDomainType.COURSE_OFFER, id);
    }
}
