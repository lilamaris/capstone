package com.lilamaris.capstone.identity.auth.domain;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "capstone_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    private User(String nickname, Instant createdAt) {
        this.nickname = Preconditions.requireNonBlank(nickname, "nickname");
        this.createdAt = Preconditions.requireNonNull(createdAt, "createdAt");
    }

    public static User of(String nickname, Instant createdAt) {
        return new User(nickname, createdAt);
    }

    public void updateNickname(String nickname) {
        this.nickname = Preconditions.requireNonBlank(nickname, "nickname");
    }
}
