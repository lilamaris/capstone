package com.lilamaris.capstone.domain.model.common.embed.impl;

import com.lilamaris.capstone.domain.model.common.embed.DomainMeta;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@Setter(AccessLevel.PROTECTED)
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JpaDomainMeta implements DomainMeta{
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    protected JpaDomainMeta(String title, String description) {
        this.title = requireField(title, "title");
        this.description = description;
    }

    public static JpaDomainMeta create(String title, String description) {
        return new JpaDomainMeta(title, description);
    }

    public static JpaDomainMeta create(String title) {
        return new JpaDomainMeta(title, null);
    }

    public void assign(DomainMeta other) {
        title = other.title();
        description = other.description();
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String description() {
        return description;
    }
}
