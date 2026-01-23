package com.lilamaris.capstone.resource_offer.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.resource_offer.domain.ResourceOffer;
import com.lilamaris.capstone.resource_offer.domain.id.ResourceOfferId;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaExternalizableId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ResourceOfferRepository extends JpaRepository<ResourceOffer, ResourceOfferId> {
    @Query("""
            SELECT CASE
            WHEN COUNT(ro) > 0
                THEN true
                ELSE false
            END
            FROM ResourceOffer ro
            WHERE ro.resource.type.name = :resourceType
                AND ro.resource.id = :resourceId
                AND ro.snapshotId = :snapshotId
            """)
    boolean existsOffer(
            String resourceType,
            String resourceId,
            JpaExternalizableId snapshotId
    );

    @Query("""
            SELECT ro
            FROM ResourceOffer ro
            WHERE ro.resource.type.name = :resourceType
                AND ro.resource.id = :resourceId
                AND ro.snapshotId = :snapshotId
            """)
    Optional<ResourceOffer> find(
            String resourceType,
            String resourceId,
            JpaExternalizableId snapshotId
    );

    @Query("""
            DELETE FROM ResourceOffer ro
            WHERE ro.resource.type.name = :resourceType
                AND ro.resource.id = :resourceId
                AND ro.snapshotId = :snapshotId
            """)
    void deleteOffer(
            String resourceType,
            String resourceId,
            JpaExternalizableId snapshotId
    );
}
