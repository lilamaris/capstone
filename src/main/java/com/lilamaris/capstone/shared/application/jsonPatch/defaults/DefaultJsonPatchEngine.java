package com.lilamaris.capstone.shared.application.jsonPatch.defaults;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.diff.JsonDiff;
import com.lilamaris.capstone.shared.application.exception.ApplicationInvariantException;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DefaultJsonPatchEngine implements JsonPatchEngine {
    private final ObjectMapper mapper;

    @Override
    public JsonNode parseNode(Object target) {
        return mapper.valueToTree(target);
    }

    @Override
    public JsonNode parseNode(String target) {
        try {
            return mapper.readTree(target);
        } catch (JsonProcessingException e) {
            throw new ApplicationInvariantException(
                    "JSON_PROCESSING_FAILED",
                    "Json processing failed."
            );
        }
    }

    @Override
    public JsonPatch parsePatch(String target) {
        try {
            JsonNode node = mapper.readTree(target);
            return JsonPatch.fromJson(node);
        } catch (IOException e) {
            throw new ApplicationInvariantException(
                    "JSON_PATCH_FAILED",
                    "Json patch failed."
            );
        }
    }

    @Override
    public String stringify(Object node) {
        try {
            return mapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new ApplicationInvariantException(
                    "JSON_PROCESSING_FAILED",
                    "Json processing failed."
            );
        }
    }

    @Override
    public JsonNode materialize(JsonPatch patch) {
        try {
            return patch.apply(mapper.createObjectNode());
        } catch (JsonPatchException e) {
            throw new ApplicationInvariantException(
                    "JSON_PATCH_FAILED",
                    "Json patch failed."
            );
        }
    }

    @Override
    public JsonPatch diff(JsonNode before, JsonNode after) {
        try {
            JsonNode diffNode = JsonDiff.asJson(
                    before,
                    after
            );
            return JsonPatch.fromJson(diffNode);
        } catch (IOException e) {
            throw new ApplicationInvariantException(
                    "JSON_PATCH_FAILED",
                    "Json patch failed."
            );
        }
    }

    @Override
    public JsonNode apply(JsonNode current, JsonPatch patch) {
        try {
            return patch.apply(current);
        } catch (JsonPatchException e) {
            throw new ApplicationInvariantException(
                    "JSON_PATCH_FAILED",
                    "Json patch failed."
            );
        }
    }
}
