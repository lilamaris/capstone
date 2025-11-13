package com.lilamaris.capstone.domain.configuration;

import com.lilamaris.capstone.domain.BaseDomain;

import java.util.HashMap;
import java.util.Map;

public class DomainTypeRegistry {
    private final Map<String, Class<? extends BaseDomain<?, ?>>> nameToClass = new HashMap<>();
    private final Map<Class<? extends BaseDomain<?, ?>>, String> classToName = new HashMap<>();


    public void register(String name, Class<? extends BaseDomain<?, ?>> clazz) {
        nameToClass.put(name, clazz);
        classToName.put(clazz, name);
    }

    public Class<? extends BaseDomain<?, ?>> classOf(String name) {
        return nameToClass.get(name);
    }

    public String nameOf(Class<? extends BaseDomain<?, ?>> clazz) {
        return classToName.get(clazz);
    }
}
