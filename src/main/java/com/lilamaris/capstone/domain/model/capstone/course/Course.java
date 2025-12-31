package com.lilamaris.capstone.domain.model.capstone.course;

import com.lilamaris.capstone.domain.model.capstone.course.id.CourseId;
import com.lilamaris.capstone.domain.model.common.domain.contract.Auditable;
import com.lilamaris.capstone.domain.model.common.domain.contract.Describable;
import com.lilamaris.capstone.domain.model.common.domain.contract.Identifiable;
import com.lilamaris.capstone.domain.model.common.domain.event.DomainEvent;
import com.lilamaris.capstone.domain.model.common.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.domain.model.common.domain.metadata.DescriptionMetadata;
import com.lilamaris.capstone.domain.model.common.infra.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.domain.model.common.infra.persistence.jpa.JpaDescriptionMetadata;
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
public class Course implements Identifiable<CourseId>, Describable, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private CourseId id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Embedded
    private JpaDescriptionMetadata descriptionMetadata;

    private Course(CourseId id, JpaDescriptionMetadata descriptionMetadata) {
        this.id = requireField(id, "id");
        this.descriptionMetadata = requireField(descriptionMetadata, "metadata");
    }

    public static Course create(String title, String details) {
        var descriptionMetadata = JpaDescriptionMetadata.create(title, details);
        return new Course(CourseId.newId(), descriptionMetadata);
    }

    @Override
    public CourseId id() {
        return id;
    }

    @Override
    public DescriptionMetadata descriptionMetadata() {
        return descriptionMetadata;
    }

    @Override
    public void updateDescription(DescriptionMetadata descriptionMetadata) {
        this.descriptionMetadata = JpaDescriptionMetadata.from(descriptionMetadata);
    }

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }
}
