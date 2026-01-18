package com.lilamaris.capstone.shared.application.support;

public interface DefinitionRegistry<K, V> {
    V definitionOf(K key);
}
