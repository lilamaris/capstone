package com.lilamaris.capstone.timeline.application.result;

import com.lilamaris.capstone.shared.application.result.AuditResult;
import com.lilamaris.capstone.shared.application.result.DescriptionResult;
import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.util.List;

public class TimelineResult {
    public record CommandCompressed(
            TimelineId id,
            DescriptionResult description,
            AuditResult audit
    ) {
        public static CommandCompressed from(Timeline domain) {
            return new CommandCompressed(
                    domain.id(),
                    DescriptionResult.from(domain),
                    AuditResult.from(domain)
            );
        }
    }

    public record Command(
            TimelineId id,
            List<SlotResult> slotList,
            DescriptionResult description,
            AuditResult audit
    ) {
        public static Command from(Timeline domain) {
            var slotList = domain.getSlotList().stream().map(SlotResult::from).toList();
            return new Command(
                    domain.id(),
                    slotList,
                    DescriptionResult.from(domain),
                    AuditResult.from(domain)
            );
        }
    }

    public record Query(
            TimelineId id,
            DescriptionResult description,
            AuditResult audit
    ) {
        public static Query from(Timeline domain) {
            return new Query(
                    domain.id(),
                    DescriptionResult.from(domain),
                    AuditResult.from(domain)
            );
        }
    }
}
