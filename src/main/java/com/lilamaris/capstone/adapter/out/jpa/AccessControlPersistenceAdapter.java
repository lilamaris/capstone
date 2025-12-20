package com.lilamaris.capstone.adapter.out.jpa;

import com.lilamaris.capstone.adapter.out.jpa.mapper.AccessControlEntityMapper;
import com.lilamaris.capstone.adapter.out.jpa.repository.AccessControlRepository;
import com.lilamaris.capstone.application.port.out.AccessControlPort;
import com.lilamaris.capstone.domain.access_control.AccessControl;
import com.lilamaris.capstone.domain.embed.DomainRef;
import com.lilamaris.capstone.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccessControlPersistenceAdapter implements AccessControlPort {
    private final AccessControlRepository repository;

    @Override
    public boolean hasGrant(User.Id userId, DomainRef domainRef, String scopedRole) {
        return repository.existsByUserIdAndResourceIdAndResourceTypeAndScopedRole(
                userId.getValue(),
                domainRef.id(),
                domainRef.type(),
                scopedRole
        );
    }

    @Override
    public Optional<AccessControl> getById(AccessControl.Id id) {
        return repository.findById(id.getValue()).map(AccessControlEntityMapper::toDomain);
    }

    @Override
    public void delete(AccessControl.Id id) {
        repository.deleteById(id.getValue());
    }

    @Override
    public AccessControl save(AccessControl domain) {
        var entity = AccessControlEntityMapper.toEntity(domain);
        var saved = repository.save(entity);
        return AccessControlEntityMapper.toDomain(saved);
    }
}
