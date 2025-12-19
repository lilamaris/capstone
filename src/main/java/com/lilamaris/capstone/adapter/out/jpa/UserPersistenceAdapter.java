package com.lilamaris.capstone.adapter.out.jpa;

import com.lilamaris.capstone.adapter.out.jpa.mapper.AccountEntityMapper;
import com.lilamaris.capstone.adapter.out.jpa.mapper.UserEntityMapper;
import com.lilamaris.capstone.adapter.out.jpa.repository.AccountRepository;
import com.lilamaris.capstone.adapter.out.jpa.repository.UserRepository;
import com.lilamaris.capstone.application.port.out.AuthPort;
import com.lilamaris.capstone.application.port.out.UserPort;
import com.lilamaris.capstone.domain.user.Account;
import com.lilamaris.capstone.domain.user.Provider;
import com.lilamaris.capstone.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort, AuthPort {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public boolean isExists(Provider provider, String providerId) {
        return accountRepository.existsByProviderAndProviderId(provider, providerId);
    }

    @Override
    public Optional<Account> getBy(Provider provider, String providerId) {
        return accountRepository.findByProviderAndProviderId(provider, providerId).map(AccountEntityMapper::toDomain);
    }

    @Override
    public Optional<User> getById(User.Id id) {
        return userRepository.findById(id.getValue()).map(UserEntityMapper::toDomain);
    }

    @Override
    public User save(User domain) {
        var entity = UserEntityMapper.toEntity(domain);
        var saved = userRepository.save(entity);
        return UserEntityMapper.toDomain(saved);
    }
}
