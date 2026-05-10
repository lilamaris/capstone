package com.lilamaris.capstone.identity.auth.application.role.port.in;

import com.lilamaris.capstone.identity.auth.application.role.port.in.query.ListUserGrantedRoleQuery;
import com.lilamaris.capstone.identity.auth.application.role.port.in.result.UserGrantedRoleResult;

import java.util.List;

public interface ListUserGrantedRoleUseCase {
    List<UserGrantedRoleResult> list(ListUserGrantedRoleQuery query);
}
