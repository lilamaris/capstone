package com.lilamaris.capstone.membership.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.membership.domain.Membership;
import com.lilamaris.capstone.membership.domain.id.MembershipId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, MembershipId> {
}
