package com.lilamaris.capstone.domain.model.capstone.course;

import com.lilamaris.capstone.domain.model.capstone.course.id.CourseId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.common.DomainRef;
import com.lilamaris.capstone.domain.model.common.ResourceDomainType;
import com.lilamaris.capstone.domain.model.common.impl.DefaultDomainRef;
import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultAuditableDomain;
import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDomainMeta;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import com.lilamaris.capstone.domain.model.common.mixin.Referenceable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@Entity
@Table(name = "course_root")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends JpaDefaultAuditableDomain implements Identifiable<CourseId>, Referenceable {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private CourseId id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "course_id", nullable = false)
    private List<CourseOffer> courseOfferList;

    @Embedded
    private JpaDomainMeta metadata;

    private Course(CourseId id, List<CourseOffer> courseOfferList, JpaDomainMeta metadata) {
        this.id = requireField(id, "id");
        this.courseOfferList = requireField(courseOfferList, "courseOfferList");
        this.metadata = requireField(metadata, "metadata");
    }

    public static Course create(String title, String description) {
        var metadata = JpaDomainMeta.create(title, description);
        return new Course(CourseId.newId(), new ArrayList<>(), metadata);
    }

    public void offer(SnapshotId snapshotId, Integer credit) {
        var offer = CourseOffer.create(id, snapshotId, credit);
        courseOfferList.add(offer);
    }

    public void updateMetadata(String title, String description) {
        var newMeta = JpaDomainMeta.create(title, description);
        metadata.assign(newMeta);
    }

    @Override
    public CourseId id() {
        return id;
    }

    @Override
    public DomainRef ref() {
        return DefaultDomainRef.from(ResourceDomainType.COURSE, id);
    }
}
