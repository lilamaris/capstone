package com.lilamaris.capstone.refresh_token.domain.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.shared.domain.defaults.DefaultStringDomainId;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenId extends DefaultStringDomainId implements ExternalizableId {
    @JsonValue
    protected String value;

    public RefreshTokenId(String value) {
        super(value);
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    protected void init(String value) {
        this.value = value;
    }

    @Override
    public String asString() {
        return value;
    }
}
