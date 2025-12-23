package com.lilamaris.capstone.adapter.in.web.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TimelineRequest {
    public record Create(String description) {
    }

    public record Update(String description) {
    }

    public record Migrate(@NotNull LocalDateTime validAt, String description) {
    }

    public record Merge(@NotNull LocalDateTime validFrom, @NotNull LocalDateTime validTo, String description) {
    }

    public record Rollback(@NotNull LocalDateTime targetTxAt, String description) {
    }
}
