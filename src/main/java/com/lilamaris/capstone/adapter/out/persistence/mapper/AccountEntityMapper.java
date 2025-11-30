package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.AccountEntity;
import com.lilamaris.capstone.domain.user.Account;
import com.lilamaris.capstone.domain.user.User;

public class AccountEntityMapper {
    public static Account toDomain(AccountEntity entity) {
        var id = Account.Id.from(entity.getId());
        var userId = User.Id.from(entity.getUserId());
        var audit = AuditEmbeddableEntityMapper.toDomain(entity);

        return Account.from(
                id,
                userId,
                entity.getProvider(),
                entity.getProviderId(),
                entity.getDisplayName(),
                entity.getEmail(),
                entity.getPasswordHash(),
                audit
        );
    }

    public static AccountEntity toEntity(Account domain) {
        return AccountEntity.builder()
                .id(domain.id().value())
                .userId(domain.userId().value())
                .provider(domain.provider())
                .providerId(domain.providerId())
                .displayName(domain.displayName())
                .email(domain.email())
                .passwordHash(domain.passwordHash())
                .build();
    }
}
