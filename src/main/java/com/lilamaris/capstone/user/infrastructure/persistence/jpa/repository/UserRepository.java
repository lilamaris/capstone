package com.lilamaris.capstone.user.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.user.domain.User;
import com.lilamaris.capstone.user.domain.id.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UserId> {
}
