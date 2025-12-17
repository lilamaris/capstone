package com.lilamaris.capstone.adapter.out.jpa.repository;

import com.lilamaris.capstone.adapter.out.jpa.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {
}
