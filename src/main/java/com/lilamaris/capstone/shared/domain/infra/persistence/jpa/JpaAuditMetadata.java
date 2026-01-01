package com.lilamaris.capstone.shared.domain.infra.persistence.jpa;

import com.lilamaris.capstone.shared.domain.defaults.DefaultAuditMetadata;
import com.lilamaris.capstone.shared.domain.infra.ToPojo;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@ToString
@Embeddable
@NoArgsConstructor
public class JpaAuditMetadata implements AuditMetadata, ToPojo<AuditMetadata> {
    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    protected JpaAuditMetadata(Instant createdAt, Instant updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static JpaAuditMetadata from(AuditMetadata auditMetadata) {
        return new JpaAuditMetadata(
                auditMetadata.createdAt(),
                auditMetadata.updatedAt()
        );
    }

    @Override
    public Instant createdAt() {
        return createdAt;
    }

    @Override
    public Instant updatedAt() {
        return updatedAt;
    }

    @Override
    public AuditMetadata toPOJO() {
        return new DefaultAuditMetadata(createdAt, updatedAt);
    }
}
