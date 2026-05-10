package com.lilamaris.capstone.identity.auth.persistence.jwks.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.DefaultResourceLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
@DisplayName("FileBasedKeyStore 테스트")
class FileBasedKeyStoreTest {
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("active 키는 서명 가능하고 public-only 키도 검증 목록에 포함한다")
    void load_active_signable_key_and_public_only_verification_key() throws Exception {
        var active = writeKeyFiles("active", generateRsaKeyPair(), true);
        var rotated = writeKeyFiles("rotated", generateRsaKeyPair(), false);

        var keyStore = new FileBasedKeyStore(
                properties("active", Set.of(active.entry(), rotated.entry())),
                new DefaultResourceLoader()
        );

        assertThat(keyStore.findSignableKey().kid()).isEqualTo("active");
        assertThat(keyStore.findVerifiableKeys())
                .extracting(key -> key.kid())
                .containsExactlyInAnyOrder("active", "rotated");
    }

    @Test
    @DisplayName("active 키에 private key가 없으면 생성 시점에 실패한다")
    void fail_when_active_key_has_no_private_key() throws Exception {
        var active = writeKeyFiles("active", generateRsaKeyPair(), false);

        assertThatThrownBy(() -> new FileBasedKeyStore(
                properties("active", Set.of(active.entry())),
                new DefaultResourceLoader()
        ))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("active signable key must have a private key");
    }

    @Test
    @DisplayName("public/private 키 쌍이 맞지 않으면 생성 시점에 실패한다")
    void fail_when_public_key_does_not_match_private_key() throws Exception {
        var publicKeyPair = generateRsaKeyPair();
        var privateKeyPair = generateRsaKeyPair();
        var publicKey = writePublicKey("active", publicKeyPair);
        var privateKey = writePrivateKey("active", privateKeyPair);
        var entry = new JwksFileProperties.KeyEntry("active", location(publicKey), location(privateKey));

        assertThatThrownBy(() -> new FileBasedKeyStore(
                properties("active", Set.of(entry)),
                new DefaultResourceLoader()
        ))
                .isInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("public key does not match private key. kid=active");
    }

    @Test
    @DisplayName("active kid가 키 목록에 없으면 생성 시점에 실패한다")
    void fail_when_active_kid_does_not_exist() throws Exception {
        var rotated = writeKeyFiles("rotated", generateRsaKeyPair(), false);

        assertThatThrownBy(() -> new FileBasedKeyStore(
                properties("active", Set.of(rotated.entry())),
                new DefaultResourceLoader()
        ))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("no active signable key exists. kid=active");
    }

    private JwksFileProperties properties(String activeSignableKid, Set<JwksFileProperties.KeyEntry> entries) {
        return new JwksFileProperties(activeSignableKid, entries);
    }

    private KeyFiles writeKeyFiles(String kid, KeyPair keyPair, boolean includePrivateKey) throws Exception {
        var publicKey = writePublicKey(kid, keyPair);
        var privateKey = includePrivateKey ? writePrivateKey(kid, keyPair) : null;
        return new KeyFiles(
                new JwksFileProperties.KeyEntry(
                        kid,
                        location(publicKey),
                        privateKey == null ? null : location(privateKey)
                )
        );
    }

    private Path writePublicKey(String kid, KeyPair keyPair) throws Exception {
        var path = tempDir.resolve(kid + "-public.pem");
        Files.writeString(path, pem("PUBLIC KEY", keyPair.getPublic().getEncoded()));
        return path;
    }

    private Path writePrivateKey(String kid, KeyPair keyPair) throws Exception {
        var path = tempDir.resolve(kid + "-private.pem");
        Files.writeString(path, pem("PRIVATE KEY", keyPair.getPrivate().getEncoded()));
        return path;
    }

    private KeyPair generateRsaKeyPair() throws Exception {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    private String pem(String label, byte[] encoded) {
        var body = Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(encoded);
        return "-----BEGIN " + label + "-----\n" + body + "\n-----END " + label + "-----\n";
    }

    private String location(Path path) {
        return path.toUri().toString();
    }

    private record KeyFiles(JwksFileProperties.KeyEntry entry) {
    }
}
