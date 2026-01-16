package com.lilamaris.capstone.snapshot.infrastructure.web.request;

import jakarta.validation.constraints.NotNull;

public class SnapshotRequest {
    public record Create(@NotNull String title, @NotNull String details) {
    }

    public record Update(String title, String details) {
    }
}
