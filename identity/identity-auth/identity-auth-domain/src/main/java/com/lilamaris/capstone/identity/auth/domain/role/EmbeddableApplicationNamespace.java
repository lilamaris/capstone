package com.lilamaris.capstone.identity.auth.domain.role;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmbeddableApplicationNamespace implements ApplicationNamespace {
    @Column(name = "name", nullable = false)
    private String name;

    private EmbeddableApplicationNamespace(String name) {
        this.name = Preconditions.requireNonBlank(name, "name");
    }

    public static EmbeddableApplicationNamespace of(String name) {
        return new EmbeddableApplicationNamespace(name);
    }

    public static EmbeddableApplicationNamespace from(ApplicationNamespace namespace) {
        Preconditions.requireNonNull(namespace, "namespace");
        return of(namespace.name());
    }

    @Override
    public String name() {
        return name;
    }
}
