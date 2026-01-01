package com.lilamaris.capstone.user.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.user.application.port.out.UserPort;
import com.lilamaris.capstone.user.domain.User;
import com.lilamaris.capstone.user.domain.id.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, UserPort {
    @Override
    Optional<User> getById(UserId id);
}
