package com.lilamaris.capstone.application.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;


public class JsonPatchEngine {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T applyJsonPatch(String jsonPatch, T target, Class<T> type) {
        try {
            JsonNode patchNode = mapper.readTree(jsonPatch);
            JsonPatch patch = JsonPatch.fromJson(patchNode);
            JsonNode targetNode = mapper.valueToTree(target);
            JsonNode patchedNode = patch.apply(targetNode);
            return mapper.treeToValue(patchedNode, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to apply JSON Patch ", e);
        }
    }

    public static <T> T applyJsonPatch(String jsonPatch, Class<T> type) {
        try {
            JsonNode patchNode = mapper.readTree(jsonPatch);
            return mapper.treeToValue(patchNode, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to apply JSON Patch ", e);
        }
    }

    public static <T> String createAddPatch(T target) {
        try {
            return mapper.writeValueAsString(target);
        } catch (Exception e) {
            throw new RuntimeException("Failed to apply JSON Patch ", e);
        }
    }
}