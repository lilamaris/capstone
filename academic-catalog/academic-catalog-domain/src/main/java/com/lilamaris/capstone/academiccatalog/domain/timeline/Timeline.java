package com.lilamaris.capstone.academiccatalog.domain.timeline;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "timeline")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timeline {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    private Timeline(String title, String description, Instant createdAt) {
        this.title = Preconditions.requireNonBlank(title, "title");
        this.description = Preconditions.requireNonBlank(description, "description");
        this.createdAt = Preconditions.requireNonNull(createdAt, "createdAt");
    }

    public static Timeline of(String title, String description, Instant createdAt) {
        return new Timeline(title, description, createdAt);
    }

    public void updateTitle(String title) {
        this.title = Preconditions.requireNonBlank(title, "title");
    }

    public void updateDescription(String description) {
        this.description = Preconditions.requireNonBlank(description, "description");
    }
}
