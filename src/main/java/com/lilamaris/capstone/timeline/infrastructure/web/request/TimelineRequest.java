package com.lilamaris.capstone.timeline.infrastructure.web.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class TimelineRequest {
    public record Create(@NotNull String title, String details, @NotNull Instant validAt) {
    }

    public record Update(String title, String details) {
    }

    public record Migrate(@NotNull Instant validAt) {
    }

    public record Merge(@NotNull Instant validFrom, @NotNull Instant validTo) {
    }
}
