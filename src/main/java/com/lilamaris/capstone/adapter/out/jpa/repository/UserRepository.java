package com.lilamaris.capstone.adapter.out.jpa.repository;

import com.lilamaris.capstone.application.port.out.UserPort;
import com.lilamaris.capstone.domain.model.capstone.user.User;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, UserPort {
    @Override
    Optional<User> getById(UserId id);
}
