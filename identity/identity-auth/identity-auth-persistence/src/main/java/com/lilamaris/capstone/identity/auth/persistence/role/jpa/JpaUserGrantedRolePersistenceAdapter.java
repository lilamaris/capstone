package com.lilamaris.capstone.identity.auth.persistence.role.jpa;

import com.lilamaris.capstone.identity.auth.application.role.port.out.UserGrantedRoleReader;
import com.lilamaris.capstone.identity.auth.application.role.port.out.UserGrantedRoleStore;
import com.lilamaris.capstone.identity.auth.application.role.port.out.criteria.UserGrantRoleLookupCriteria;
import com.lilamaris.capstone.identity.auth.domain.role.UserGrantedRole;
import com.lilamaris.capstone.identity.auth.persistence.role.jpa.repository.UserGrantedRoleRepository;
import com.lilamaris.capstone.identity.auth.persistence.shared.jpa.specification.CommonPredicates;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaUserGrantedRolePersistenceAdapter implements UserGrantedRoleStore, UserGrantedRoleReader {
    private final UserGrantedRoleRepository repository;

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByCriteria(UserGrantRoleLookupCriteria criteria) {
        return repository.exists(toSpecification(criteria));
    }

    @Override
    public Optional<UserGrantedRole> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<UserGrantedRole> findByCriteria(UserGrantRoleLookupCriteria criteria) {
        return repository.findOne(toSpecification(criteria));
    }

    @Override
    public List<UserGrantedRole> findByUserId(UUID userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public UserGrantedRole save(UserGrantedRole userGrantedRole) {
        return repository.save(userGrantedRole);
    }

    @Override
    public List<UserGrantedRole> saveAll(Collection<UserGrantedRole> userGrantedRoles) {
        return repository.saveAll(userGrantedRoles);
    }

    @Override
    public void delete(UserGrantedRole userGrantedRole) {
        repository.delete(userGrantedRole);
    }

    private Specification<UserGrantedRole> toSpecification(UserGrantRoleLookupCriteria criteria) {
        return Specification.<UserGrantedRole>unrestricted()
                .and(CommonPredicates.eq(criteria.userId(), from -> from.get("userId")))
                .and(CommonPredicates.eq(criteria.namespace().name(), from -> from.get("namespace").get("name")))
                .and(CommonPredicates.eq(criteria.role(), from -> from.get("role")));
    }
}
