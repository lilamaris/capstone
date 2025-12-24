package com.lilamaris.capstone.domain.model.common.embed.impl;

import com.lilamaris.capstone.domain.model.common.embed.DomainMeta;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DefaultDomainMeta implements DomainMeta {
    private String title;
    private String description;

    @Override
    public String title() {
        return title;
    }

    @Override
    public String description() {
        return description;
    }
}
