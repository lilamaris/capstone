package com.lilamaris.capstone.bootstrap.webmvc.advice.resolver;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import lombok.RequiredArgsConstructor;

import java.net.URI;

@RequiredArgsConstructor
public class TypeUriResolver {
    private final String typeBaseUri;

    public URI resolve(String path) {
        Preconditions.requireNonBlank(path, "path");

        var baseUri = typeBaseUri.endsWith("/") ? typeBaseUri : typeBaseUri + "/";
        return URI.create(baseUri).resolve(path);
    }
}
