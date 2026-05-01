package com.lilamaris.capstone.identity.auth.application.role.port.out;

import com.lilamaris.capstone.identity.auth.domain.role.UserGrantedRole;

import java.util.Collection;
import java.util.List;

public interface UserGrantedRoleStore {
    UserGrantedRole save(UserGrantedRole userGrantedRole);

    List<UserGrantedRole> saveAll(Collection<UserGrantedRole> userGrantedRoles);

    void delete(UserGrantedRole userGrantedRole);
}
