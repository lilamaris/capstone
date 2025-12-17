package com.lilamaris.capstone.adapter.out.jpa.repository;

import com.lilamaris.capstone.adapter.out.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
