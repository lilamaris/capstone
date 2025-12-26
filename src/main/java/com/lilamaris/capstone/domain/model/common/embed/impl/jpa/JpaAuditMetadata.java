package com.lilamaris.capstone.domain.model.common.embed.impl.jpa;

import com.lilamaris.capstone.domain.model.common.embed.AuditMetadata;
import com.lilamaris.capstone.domain.model.common.embed.impl.DefaultAuditMetadata;
import com.lilamaris.capstone.domain.model.common.mixin.ToPojo;
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
