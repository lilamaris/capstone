package com.lilamaris.capstone.course.application.service;

import com.lilamaris.capstone.course.application.port.in.CourseCommandUseCase;
import com.lilamaris.capstone.course.application.port.out.CoursePort;
import com.lilamaris.capstone.course.application.result.CourseResult;
import com.lilamaris.capstone.course.domain.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseCommandService implements CourseCommandUseCase {
    private final CoursePort coursePort;

    @Override
    public CourseResult.Command create(String code, String name) {
        var created = Course.create(code, name);
        var saved = coursePort.save(created);
        return CourseResult.Command.from(saved);
    }
}
