package com.lilamaris.capstone.adapter.in.web.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TimelineRequest {
    public record Create(@NotNull String title, String details, @NotNull LocalDateTime initialValidAt) {
    }

    public record Update(String title, String details) {
    }

    public record Migrate(@NotNull LocalDateTime validAt) {
    }

    public record Merge(@NotNull LocalDateTime validFrom, @NotNull LocalDateTime validTo) {
    }
}
