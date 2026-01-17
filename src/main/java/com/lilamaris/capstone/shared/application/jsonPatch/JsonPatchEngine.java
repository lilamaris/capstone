package com.lilamaris.capstone.shared.application.jsonPatch;

public interface JsonPatchEngine {
    <T> T fromPatch(String patch, Class<T> clazz);

    <T> T apply(String patch, T target, Class<T> clazz);

    <T> String createPatch(T target);
}
