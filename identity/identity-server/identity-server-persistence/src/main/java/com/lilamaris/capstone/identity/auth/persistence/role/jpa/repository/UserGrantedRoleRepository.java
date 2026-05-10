package com.lilamaris.capstone.identity.auth.persistence.role.jpa.repository;

import com.lilamaris.capstone.identity.auth.domain.role.UserGrantedRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface UserGrantedRoleRepository extends JpaRepository<UserGrantedRole, UUID>, JpaSpecificationExecutor<UserGrantedRole> {
    List<UserGrantedRole> findByUserId(UUID userId);
}
