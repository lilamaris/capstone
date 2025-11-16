package com.lilamaris.capstone.adapter.in.web.request;

import java.util.UUID;

public class CourseRequest {
    public record Create(String code, String name, Integer credit) {}
    public record CreateOffer(Integer semester, UUID courseId, UUID snapshotId, UUID timelineId) {}
}
