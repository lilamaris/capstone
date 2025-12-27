package com.lilamaris.capstone.domain.model.capstone.course;

import com.lilamaris.capstone.domain.model.capstone.course.id.CourseId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.common.infra.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.domain.model.common.infra.persistence.jpa.JpaDescriptionMetadata;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.defaults.DefaultDomainRef;
import com.lilamaris.capstone.domain.model.common.domain.contract.Identifiable;
import com.lilamaris.capstone.domain.model.common.domain.contract.Referenceable;
import com.lilamaris.capstone.domain.model.common.domain.type.ResourceDomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "course_root")
@EntityListeners(AuditingEntityListener.class)
public class Course implements Identifiable<CourseId>, Referenceable {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private CourseId id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "course_id", nullable = false)
    private List<CourseOffer> courseOfferList;

    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Embedded
    private JpaDescriptionMetadata descriptionMetadata;

    private Course(CourseId id, List<CourseOffer> courseOfferList, JpaDescriptionMetadata descriptionMetadata) {
        this.id = requireField(id, "id");
        this.courseOfferList = requireField(courseOfferList, "courseOfferList");
        this.descriptionMetadata = requireField(descriptionMetadata, "metadata");
    }

    public static Course create(String title, String details) {
        var descriptionMetadata = JpaDescriptionMetadata.create(title, details);
        return new Course(CourseId.newId(), new ArrayList<>(), descriptionMetadata);
    }

    public void offer(SnapshotId snapshotId, Integer credit) {
        var offer = CourseOffer.create(id, snapshotId, credit);
        courseOfferList.add(offer);
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
