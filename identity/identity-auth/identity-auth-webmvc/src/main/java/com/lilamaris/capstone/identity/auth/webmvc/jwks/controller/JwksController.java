package com.lilamaris.capstone.identity.auth.webmvc.jwks.controller;

import com.lilamaris.capstone.identity.auth.application.jwks.port.in.ListVerifiableKeyUseCase;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "JWKS", description = "JWT 검증용 공개키 조회 API")
@RestController
@RequestMapping("/.well-known")
@RequiredArgsConstructor
public class JwksController {
    private final ListVerifiableKeyUseCase listVerifiableKeyUseCase;

    @GetMapping("jwks.json")
    public Map<String, Object> listJwks() {
        var verificationKey = listVerifiableKeyUseCase.list();

        var keys = verificationKey.stream()
                .map(key ->
                        new RSAKey.Builder(key.getPublicKey())
                                .keyID(key.kid())
                                .keyUse(KeyUse.SIGNATURE)
                                .algorithm(JWSAlgorithm.RS256)
                                .build()
                )
                .map(RSAKey::toJSONObject)
                .toList();

        return Map.of("keys", keys);
    }
}
