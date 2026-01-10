package com.lilamaris.capstone.membership.infrastructure.persistence.jpa;

import com.lilamaris.capstone.membership.application.port.out.MembershipPort;
import com.lilamaris.capstone.membership.domain.Membership;
import com.lilamaris.capstone.membership.domain.id.MembershipId;
import com.lilamaris.capstone.membership.infrastructure.persistence.jpa.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MembershipPersistenceAdapter implements MembershipPort {
    private final MembershipRepository repository;

    @Override
    public Optional<Membership> getById(MembershipId id) {
        return repository.findById(id);
    }

    @Override
    public Membership save(Membership domain) {
        return repository.save(domain);
    }

    @Override
    public void delete(MembershipId id) {
        repository.deleteById(id);
    }
}
