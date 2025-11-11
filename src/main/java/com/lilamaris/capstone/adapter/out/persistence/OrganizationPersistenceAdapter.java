package com.lilamaris.capstone.adapter.out.persistence;

import com.lilamaris.capstone.adapter.out.persistence.entity.OrganizationEntity;
import com.lilamaris.capstone.adapter.out.persistence.mapper.OrganizationEntityMapper;
import com.lilamaris.capstone.adapter.out.persistence.repository.OrganizationRepository;
import com.lilamaris.capstone.application.port.out.OrganizationPort;
import com.lilamaris.capstone.domain.degree.Organization;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrganizationPersistenceAdapter implements OrganizationPort {
    private final OrganizationRepository repository;
    private final EntityManager em;

    @Override
    public Optional<Organization> getById(Organization.Id id) {
        return repository.findById(id.value()).map(OrganizationEntityMapper::toDomain);
    }

    @Override
    public List<Organization> getByGroupId(Organization.GroupId groupId) {
        return repository.findByGroupId(groupId.value()).stream().map(OrganizationEntityMapper::toDomain).toList();
    }

    @Override
    public Organization save(Organization domain) {
        var entity = OrganizationEntityMapper.toEntity(domain);
        OrganizationEntity result;

        if (!em.contains(entity)) {
            boolean exists = entity.getId() != null && em.find(OrganizationEntity.class, entity.getId()) != null;
            if (exists) {
                result = em.merge(entity);
            } else {
                em.persist(entity);
                result = entity;
            }
        } else {
            result = repository.save(entity);
        }

        return OrganizationEntityMapper.toDomain(result);
    }
}
