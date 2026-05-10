package com.lilamaris.capstone.identity.auth.application.role.port.out;

import com.lilamaris.capstone.identity.auth.application.role.port.out.criteria.UserGrantRoleLookupCriteria;
import com.lilamaris.capstone.identity.auth.domain.role.UserGrantedRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGrantedRoleReader {
    boolean existsById(UUID id);

    boolean existsByCriteria(UserGrantRoleLookupCriteria criteria);

    Optional<UserGrantedRole> findById(UUID id);

    Optional<UserGrantedRole> findByCriteria(UserGrantRoleLookupCriteria criteria);

    List<UserGrantedRole> findByUserId(UUID userId);
}
