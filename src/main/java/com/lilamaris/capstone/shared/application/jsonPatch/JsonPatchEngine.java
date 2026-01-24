package com.lilamaris.capstone.shared.application.jsonPatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;

public interface JsonPatchEngine {
    JsonNode parseNode(Object target);

    JsonNode parseNode(String target);

    JsonPatch parsePatch(String target);

    String stringify(Object node);

    JsonNode materialize(JsonPatch jsonPatch);

    JsonPatch diff(JsonNode before, JsonNode after);

    JsonNode apply(JsonNode current, JsonPatch patch);
}
