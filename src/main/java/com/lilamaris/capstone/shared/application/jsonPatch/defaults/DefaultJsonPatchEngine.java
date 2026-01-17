package com.lilamaris.capstone.shared.application.jsonPatch.defaults;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultJsonPatchEngine implements JsonPatchEngine {
    private final ObjectMapper mapper;

    @Override
    public <T> T fromPatch(String patch, Class<T> clazz) {
        try {
            var patchNode = mapper.readTree(patch);
            return mapper.treeToValue(patchNode, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to apply patch: ", e);
        }
    }

    @Override
    public <T> T apply(String patch, T target, Class<T> clazz) {
        try {
            var jsonPatch = JsonPatch.fromJson(mapper.readTree(patch));
            var patched = jsonPatch.apply(mapper.valueToTree(target));
            return mapper.treeToValue(patched, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to apply patch: ", e);
        }
    }

    @Override
    public <T> String createPatch(T target) {
        try {
            return mapper.writeValueAsString(target);
        } catch (Exception e) {
            throw new RuntimeException("Failed create patch: ", e);
        }
    }
}
