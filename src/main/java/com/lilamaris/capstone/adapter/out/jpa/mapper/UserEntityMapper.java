package com.lilamaris.capstone.adapter.out.jpa.mapper;

import com.lilamaris.capstone.adapter.out.jpa.entity.UserEntity;
import com.lilamaris.capstone.domain.user.User;

import java.util.stream.Collectors;

public class UserEntityMapper {
    public static User toDomain(UserEntity entity) {
        var id = User.Id.from(entity.getId());
        var accountSet = entity.getAccountSet().stream().map(AccountEntityMapper::toDomain).collect(Collectors.toSet());
        var audit = AuditEmbeddableEntityMapper.toDomain(entity);

        return User.from(id, entity.getDisplayName(), accountSet, entity.getRole(), audit);
    }

    public static UserEntity toEntity(User domain) {
        var accountSet = domain.accountSet().stream().map(AccountEntityMapper::toEntity).collect(Collectors.toSet());
        return UserEntity.builder()
                .id(domain.id().value())
                .displayName(domain.displayName())
                .accountSet(accountSet)
                .role(domain.role())
                .build();
    }
}
