package com.lilamaris.capstone.identity.auth.persistence.jwks.io;

import com.lilamaris.capstone.identity.auth.application.jwks.port.out.JwksReader;
import com.lilamaris.capstone.identity.auth.domain.jwks.RSASignatureKey;
import com.lilamaris.capstone.identity.auth.domain.jwks.RSAVerificationKey;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class FileBasedKeyStore implements JwksReader {
    private static final byte[] KEY_PAIR_CHECK_PAYLOAD = "jwks-key-pair-check".getBytes(StandardCharsets.UTF_8);

    private final String activeSignableKid;
    private final Map<String, KeyMaterial> keyMap;

    private record KeyMaterial(String kid, RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        public RSASignatureKey toSignature() {
            if (privateKey == null)
                throw new IllegalStateException("signable key does not have a private key. kid=" + kid);
            return RSASignatureKey.of(kid, privateKey);
        }

        public RSAVerificationKey toVerification() {
            return RSAVerificationKey.of(kid, publicKey);
        }
    }

    public FileBasedKeyStore(
            JwksFileProperties properties,
            ResourceLoader resourceLoader
    ) {
        Objects.requireNonNull(properties, "properties");
        Objects.requireNonNull(resourceLoader, "resourceLoader");
        validateConfigured(properties);

        this.activeSignableKid = properties.activeSignableKid();

        this.keyMap = properties.keys().stream()
                .map(entry -> loadKey(entry.kid(), entry.publicKeyLocation(), entry.privateKeyLocation(), resourceLoader))
                .collect(Collectors.toUnmodifiableMap(
                        KeyMaterial::kid,
                        Function.identity()
                ));

        validateActiveSignableKey();
    }

    @Override
    public RSASignatureKey findSignableKey() {
        var keyPair = keyMap.get(activeSignableKid);
        if (keyPair == null)
            throw new NoSuchElementException("no active signable key exists. kid=" + activeSignableKid);
        return keyPair.toSignature();
    }

    @Override
    public List<RSAVerificationKey> findVerifiableKeys() {
        return keyMap.values().stream()
                .map(KeyMaterial::toVerification)
                .toList();
    }

    private void validateConfigured(JwksFileProperties properties) {
        if (!StringUtils.hasText(properties.activeSignableKid()))
            throw new IllegalStateException("identity.jwks.active-signable-kid is not configured");
        if (properties.keys() == null || properties.keys().isEmpty())
            throw new IllegalStateException("identity.jwks.keys is not configured");

        properties.keys().forEach(entry -> {
            if (!StringUtils.hasText(entry.kid()))
                throw new IllegalStateException("identity.jwks.keys[].kid is not configured");
            if (!StringUtils.hasText(entry.publicKeyLocation()))
                throw new IllegalStateException("identity.jwks.keys[].public-key-location is not configured. kid=" + entry.kid());
        });
    }

    private void validateActiveSignableKey() {
        var key = keyMap.get(activeSignableKid);
        if (key == null)
            throw new NoSuchElementException("no active signable key exists. kid=" + activeSignableKid);
        if (key.privateKey() == null)
            throw new IllegalStateException("active signable key must have a private key. kid=" + activeSignableKid);
    }

    private KeyMaterial loadKey(String kid, String publicKeyLocation, String privateKeyLocation, ResourceLoader resourceLoader) {
        RSAPublicKey publicKey;
        RSAPrivateKey privateKey = null;

        try {
            var publicKeyResource = resourceLoader.getResource(publicKeyLocation);
            publicKey = readPublicKey(publicKeyResource);

            if (StringUtils.hasText(privateKeyLocation)) {
                var privateKeyResource = resourceLoader.getResource(privateKeyLocation);
                privateKey = readPrivateKey(privateKeyResource);
                validateKeyPair(kid, publicKey, privateKey);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load key. publicKey location: %s, privateKey location: %s".formatted(publicKeyLocation, privateKeyLocation), e);
        }

        return new KeyMaterial(kid, publicKey, privateKey);
    }

    private void validateKeyPair(String kid, RSAPublicKey publicKey, RSAPrivateKey privateKey) throws GeneralSecurityException {
        var signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(KEY_PAIR_CHECK_PAYLOAD);
        var signature = signer.sign();

        var verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(KEY_PAIR_CHECK_PAYLOAD);
        if (!verifier.verify(signature))
            throw new IllegalStateException("public key does not match private key. kid=" + kid);
    }

    private RSAPrivateKey readPrivateKey(Resource resource) throws Exception {
        String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        key = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    private RSAPublicKey readPublicKey(Resource resource) throws Exception {
        String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        key = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
    }
}
