package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.List;

public record SimpleDailySchedule(
        List<DailyNanoRange> ranges
) implements DailySchedule {
    public SimpleDailySchedule {
        ranges = DailySchedule.validateAndSort(ranges);
    }

    public static SimpleDailySchedule of(List<DailyNanoRange> ranges) {
        return new SimpleDailySchedule(ranges);
    }

    public static SimpleDailySchedule from(DailySchedule schedule) {
        Preconditions.requireNonNull(schedule, "schedule");

        return of(schedule.ranges());
    }
}