package com.lilamaris.capstone.course.domain;

import com.lilamaris.capstone.course.domain.id.CourseId;
import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Describable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.metadata.DescriptionMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaDescriptionMetadata;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "course")
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
