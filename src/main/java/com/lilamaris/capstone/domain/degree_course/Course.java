package com.lilamaris.capstone.domain.degree_course;

import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.embed.Audit;
import com.lilamaris.capstone.domain.event.CourseOfferDeltaEvent;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record Course(
        Id id,
        String code,
        String name,
        Integer credit,
        List<CourseOffer> courseOfferList,
        List<CourseOfferDeltaEvent> courseOfferEventList,
        Audit audit
) implements BaseDomain<Course.Id, Course> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public Course {
        id = Optional.ofNullable(id).orElseGet(Id::random);

        code = Optional.ofNullable(code).orElseThrow(() -> new IllegalArgumentException("'code' of type String cannot be null"));
        name = Optional.ofNullable(name).orElseThrow(() -> new IllegalArgumentException("'name' of type String cannot be null"));
        credit = Optional.ofNullable(credit).orElseThrow(() -> new IllegalArgumentException("'credit' of type Integer cannot be null"));

        courseOfferList = Optional.ofNullable(courseOfferList).orElseGet(ArrayList::new);
        courseOfferEventList = Optional.ofNullable(courseOfferEventList).orElseGet(ArrayList::new);
    }

    public static Course from(Id id, String code, String name, Integer credit, List<CourseOffer> courseOfferList, Audit audit) {
        return getDefaultBuilder(code, name, credit).id(id).courseOfferList(courseOfferList).audit(audit).build();
    }

    public static Course create(String code, String name, Integer credit) {
        return getDefaultBuilder(code, name, credit).build();
    }

    public static Course create(String code, String name, Integer credit, List<CourseOffer> courseOfferList) {
        return getDefaultBuilder(code, name, credit).courseOfferList(courseOfferList).build();
    }

    public Course offer(Integer semester) {
        var courseOffer = CourseOffer.create(this.id(), semester);
        var event = List.of(CourseOfferDeltaEvent.from(courseOffer));
        var updated = new ArrayList<>(courseOfferList);
        updated.add(courseOffer);
        return copyWithCourseOfferList(updated, event);
    }

    public List<CourseOfferDeltaEvent> pullEvent() {
        return courseOfferEventList;
    }

    public Course copyWithCourseOfferList(List<CourseOffer> courseOfferList, List<CourseOfferDeltaEvent> event) {
        return toBuilder().courseOfferList(courseOfferList).courseOfferEventList(event).build();
    }

    private static CourseBuilder getDefaultBuilder(String code, String name, Integer credit) {
        return builder().code(code).name(name).credit(credit);
    }
}
