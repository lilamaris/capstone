package com.lilamaris.capstone.course.application.resolver;

import com.fasterxml.jackson.databind.JsonNode;
import com.lilamaris.capstone.course.application.port.out.CourseStore;
import com.lilamaris.capstone.course.domain.id.CourseId;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchEngine;
import com.lilamaris.capstone.shared.application.jsonPatch.DomainJsonResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseDomainJsonResolver implements DomainJsonResolver {
    private final CourseStore courseStore;
    private final JsonPatchEngine jsonPatchEngine;
    private final DomainRefResolverDirectory refs;

    @Override
    public DomainType support() {
        return AggregateDomainType.COURSE;
    }

    @Override
    public JsonNode resolve(DomainRef ref) {
        var id = refs.resolve(ref, CourseId.class);
        var course = courseStore.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Course with id '%s' not found", id
                )));
        return jsonPatchEngine.parseNode(course);
    }
}
