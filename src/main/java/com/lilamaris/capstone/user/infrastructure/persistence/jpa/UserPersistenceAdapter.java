package com.lilamaris.capstone.user.infrastructure.persistence.jpa;

import com.lilamaris.capstone.user.application.port.out.UserStore;
import com.lilamaris.capstone.user.domain.User;
import com.lilamaris.capstone.user.domain.id.UserId;
import com.lilamaris.capstone.user.infrastructure.persistence.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserStore {
    private final UserRepository repository;

    @Override
    public Optional<User> getById(UserId id) {
        return repository.findById(id);
    }

    @Override
    public boolean isExists(UserId id) {
        return repository.existsById(id);
    }

    @Override
    public User save(User domain) {
        return repository.save(domain);
    }
}
