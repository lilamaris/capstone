package com.lilamaris.capstone.shared.domain.persistence.jpa;

import com.lilamaris.capstone.shared.domain.defaults.DefaultDomainTypeToken;
import com.lilamaris.capstone.shared.domain.persistence.ToPojo;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JpaDomainTypeToken implements DomainType, ToPojo<DomainType> {
    private String name;

    private JpaDomainTypeToken(String name) {
        this.name = name;
    }

    public static JpaDomainTypeToken from(DomainType type) {
        return new JpaDomainTypeToken(type.name());
    }

    public static JpaDomainTypeToken of(String name) {
        return new JpaDomainTypeToken(name);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public DefaultDomainTypeToken toPOJO() {
        return new DefaultDomainTypeToken(name);
    }
}
