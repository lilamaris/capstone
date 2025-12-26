package com.lilamaris.capstone.adapter.in.web.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TimelineRequest {
    public record Create(String title, String details) {
    }

    public record Update(String title, String details) {
    }

    public record Migrate(@NotNull LocalDateTime validAt, String details) {
    }

    public record Merge(@NotNull LocalDateTime validFrom, @NotNull LocalDateTime validTo, String details) {
    }

    public record Rollback(@NotNull LocalDateTime targetTxAt, String description) {
    }
}
