package com.lilamaris.capstone.identity.auth.persistence.account.jpa;

import com.lilamaris.capstone.identity.auth.application.account.port.out.UserReader;
import com.lilamaris.capstone.identity.auth.application.account.port.out.UserStore;
import com.lilamaris.capstone.identity.auth.domain.account.User;
import com.lilamaris.capstone.identity.auth.persistence.account.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaUserPersistenceAdapter implements UserStore, UserReader {
    private final UserRepository repository;

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }
}
