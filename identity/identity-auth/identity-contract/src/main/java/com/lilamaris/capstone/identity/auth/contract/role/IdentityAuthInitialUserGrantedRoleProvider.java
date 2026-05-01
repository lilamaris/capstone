package com.lilamaris.capstone.identity.auth.contract.role;

import com.lilamaris.capstone.identity.auth.contract.IdentityAuthNamespace;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import com.lilamaris.capstone.identity.core.role.InitialUserGrantedRoleProvider;
import com.lilamaris.capstone.identity.core.role.NamespaceRole;
import com.lilamaris.capstone.identity.core.role.SimpleNamespaceRole;

public final class IdentityAuthInitialUserGrantedRoleProvider implements InitialUserGrantedRoleProvider {
    @Override
    public NamespaceRole provide() {
        return new SimpleNamespaceRole(IdentityAuthNamespace.NAMESPACE, CanonicalRole.USER);
    }
}
