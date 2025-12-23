package com.lilamaris.capstone.application.port.in;

import com.lilamaris.capstone.application.port.in.result.CourseResult;
import com.lilamaris.capstone.domain.degree_course.Course;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.Timeline;

public interface CourseCommandUseCase {
    CourseResult.Command create(String code, String name, Integer credit);

    CourseResult.Command createOffer(Course.Id id, Integer semester, Timeline.Id timelineId, Snapshot.Id snapshotId);
}
