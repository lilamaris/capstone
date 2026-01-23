package com.lilamaris.capstone.snapshot.application.port.in;

public interface SnapshotCreator {
    SnapshotEntry create(String title, String details);
}
