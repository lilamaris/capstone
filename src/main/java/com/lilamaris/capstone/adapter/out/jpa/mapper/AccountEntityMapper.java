package com.lilamaris.capstone.adapter.out.jpa.mapper;

import com.lilamaris.capstone.adapter.out.jpa.entity.AccountEntity;
import com.lilamaris.capstone.domain.user.Account;
import com.lilamaris.capstone.domain.user.User;

public class AccountEntityMapper {
    public static Account toDomain(AccountEntity entity) {
        var id = new Account.Id(entity.getId());
        var userId = new User.Id(entity.getUserId());
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
                .id(domain.id().getValue())
                .userId(domain.userId().getValue())
                .provider(domain.provider())
                .providerId(domain.providerId())
                .displayName(domain.displayName())
                .email(domain.email())
                .passwordHash(domain.passwordHash())
                .build();
    }
}
