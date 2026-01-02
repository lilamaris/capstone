package com.lilamaris.capstone.shared.domain.persistence.persistence.jpa;

import com.lilamaris.capstone.shared.domain.defaults.DefaultDescriptionMetadata;
import com.lilamaris.capstone.shared.domain.metadata.DescriptionMetadata;
import com.lilamaris.capstone.shared.domain.persistence.persistence.ToPojo;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JpaDescriptionMetadata implements DescriptionMetadata, ToPojo<DescriptionMetadata> {
    private String title;
    private String details;

    protected JpaDescriptionMetadata(String title, String details) {
        this.title = requireField(title, "title");
        this.details = details;
    }

    public static JpaDescriptionMetadata create(String title) {
        return new JpaDescriptionMetadata(title, null);
    }

    public static JpaDescriptionMetadata create(String title, String details) {
        return new JpaDescriptionMetadata(title, details);
    }

    public static JpaDescriptionMetadata from(DescriptionMetadata descriptionMetadata) {
        return new JpaDescriptionMetadata(descriptionMetadata.title(), descriptionMetadata.details());
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String details() {
        return details;
    }

    @Override
    public DescriptionMetadata toPOJO() {
        return new DefaultDescriptionMetadata(title, details);
    }
}
