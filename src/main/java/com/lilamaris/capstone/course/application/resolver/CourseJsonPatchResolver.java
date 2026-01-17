package com.lilamaris.capstone.course.application.resolver;

import com.lilamaris.capstone.course.application.port.out.CourseStore;
import com.lilamaris.capstone.course.domain.Course;
import com.lilamaris.capstone.course.domain.id.CourseId;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchEngine;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseJsonPatchResolver implements JsonPatchResolver<Course> {
    private final CourseStore courseStore;
    private final JsonPatchEngine jsonPatchEngine;
    private final DomainRefResolverDirectory refs;

    @Override
    public DomainType support() {
        return AggregateDomainType.COURSE;
    }

    @Override
    public String resolve(Course domain) {
        return jsonPatchEngine.createPatch(domain);
    }

    @Override
    public String resolve(DomainRef ref) {
        var id = refs.resolve(ref, CourseId.class);
        var course = courseStore.getById(id);
        return jsonPatchEngine.createPatch(course);
    }
}
